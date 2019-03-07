package com.example.workit.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.Toast
import com.example.workit.R
import com.example.workit.adapter.TrainingAdapter
import com.example.workit.data.Etape
import com.example.workit.tools.TimerEtapeAnimation
import com.example.workit.tools.TimerView
import com.example.workit.tools.XMLDOMParser
import kotlinx.android.synthetic.main.content_show_training.*
import kotlinx.android.synthetic.main.simple_textview_perso.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Created by JustinRudat on 06/03/2019.
 */
class ShowTrainingActivity : AppCompatActivity() {
    private val EXTRA_POSITION_CHOICE = "0"
    private val EXTRA_NOM_ETAPE = "etape_string"
    private val EXTRA_DESC_ETAPE = "desc_string"
    private val EXTRA_TEMPS_ETAPE = "temps_string"
    private val EXTRA_PAUSE_ETAPE = "pause_string"
    private val EXTRA_POSITION_KEY = "key_position"
    private val EXTRA_TYPE_ACTIVITY = "activity_type"

    var testTmp = 0
    var isUp = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_training)
        total_view_show_train?.setOnClickListener {
            if (isUp) {
                if (rel_layout_edit_train != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rel_layout_edit_train != null) {
                            rel_layout_edit_train.translationY = 200f
                            isUp = false
                        }
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            if (rel_layout_edit_train != null) {
                rel_layout_edit_train.z = 5f
            }
        }
        // parser le doc des workout
        val parser = XMLDOMParser(this)
        val intent = intent
        val stream_file: FileInputStream
        try {

            //stream = getResources().openRawResource(R.raw.total_list_workout);
            // pour ecrire sur sd

            val file = File(Environment.getExternalStorageDirectory().toString() + "/Workout/total_list_workout.xml")
            stream_file = FileInputStream(file)

            val doc = parser.getDocument(stream_file)
            val strPos = intent.getStringExtra(EXTRA_POSITION_CHOICE)

            val position = Integer.parseInt(strPos)

            val nodeList = doc!!.getElementsByTagName("workout")

            val finalWorkouts = parser.getXMLWorkoutValue(nodeList)
            val wo_tmp = finalWorkouts[position]
            if (textView_lrg_training_name != null) {
                textView_lrg_training_name.text = wo_tmp.toString()
            }

            val adapter = TrainingAdapter(this@ShowTrainingActivity, wo_tmp.list_etape)
            if (listView_show_training != null) {
                listView_show_training.adapter = adapter


                listView_show_training.onItemClickListener =
                    AdapterView.OnItemClickListener { _, view, _, id ->
                    val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_LONG)
                    if (isUp) {
                        if (rel_layout_edit_train != null) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                if (rel_layout_edit_train != null) {
                                    rel_layout_edit_train.translationY = 200f
                                    isUp = false
                                }
                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 16) {
                        //Permet de ne pas recycler la vue de l'animation en cours d'execution(animation timer multiple)
                        view.setHasTransientState(true)

                    }
                        listView_show_training.isEnabled = false

                        //listView_show_training.setScrollContainer(false);

                        textView_go.visibility = View.GONE
                    view.setOnClickListener(null)
                    val pos = id.toInt()

                        if (time_etape_view != null) {
                        toast.setText("GO")
                        toast.show()
                            val timeEtapeView = time_etape_view as TimerView
                            val tmrEtpAnim = TimerEtapeAnimation(timeEtapeView, 360)
                            tmrEtpAnim.duration = (wo_tmp.list_etape[pos].temps * 1000).toLong()
                            tmrEtpAnim.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {

                            }

                            override fun onAnimationEnd(animation: Animation) {
                                timeEtapeView.paint.color = Color.rgb(254, 195, 1)
                                toast.setText("Now rest time")
                                toast.show()
                                timeEtapeView.clearAnimation()
                                timeEtapeView.angle = 0f
                                val tmrEtpAnim2 = TimerEtapeAnimation(timeEtapeView, 360)
                                tmrEtpAnim2.duration = (wo_tmp.list_etape[pos].pause * 1000).toLong()
                                tmrEtpAnim2.setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation) {}

                                    override fun onAnimationEnd(animation: Animation) {
                                        timeEtapeView.paint.color = Color.rgb(19, 142, 0)

                                        toast.setText("Done")
                                        toast.show()
                                        timeEtapeView.clearAnimation()
                                        timeEtapeView.angle = 0f
                                        val tmrEtpAnim3 = TimerEtapeAnimation(timeEtapeView, 360)
                                        tmrEtpAnim3.duration = 200

                                        timeEtapeView.startAnimation(tmrEtpAnim3)
                                        if (Build.VERSION.SDK_INT >= 16) {
                                            view.setHasTransientState(true)
                                        }
                                        listView_show_training.isEnabled = true

                                    }

                                    override fun onAnimationRepeat(animation: Animation) {}
                                })

                                timeEtapeView.startAnimation(tmrEtpAnim2)
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })

                            timeEtapeView.startAnimation(tmrEtpAnim)

                    }
                }
                listView_show_training.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { _, _, _, id ->
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rel_layout_edit_train != null) {
                            rel_layout_edit_train.translationY = -200f
                            isUp = true
                        }
                    }
                        testTmp = id.toInt()
                    true
                }
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        // fin parseur


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        try {
            if (resultCode == 1) {
                val name_etape_tmp: String?
                val desc_etape_tmp: String
                val temps_etape_tmp: Int
                val pause_etape_tmp: Int
                total_view_show_train?.setOnClickListener {
                    if (isUp) {
                        if (rel_layout_edit_train != null) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                if (rel_layout_edit_train != null) {
                                    rel_layout_edit_train.translationY = 200f
                                    isUp = false
                                }
                            }
                        }
                    }
                }
                val etap_tmp: Etape

                if (intent != null) {
                    name_etape_tmp = intent.extras!!.getString(EXTRA_NOM_ETAPE)
                    desc_etape_tmp = intent.getStringExtra(EXTRA_DESC_ETAPE)
                    temps_etape_tmp = Integer.parseInt(intent.getStringExtra(EXTRA_TEMPS_ETAPE))
                    pause_etape_tmp = Integer.parseInt(intent.getStringExtra(EXTRA_PAUSE_ETAPE))
                    val indice_key = Integer.parseInt(intent.getStringExtra(EXTRA_POSITION_KEY))
                    etap_tmp =
                        Etape(name_etape_tmp, desc_etape_tmp, temps_etape_tmp, pause_etape_tmp)
                    val indice_workout = Integer.parseInt(getIntent().getStringExtra(EXTRA_POSITION_CHOICE))
                    val file =
                        File(Environment.getExternalStorageDirectory().toString() + "/Workout/total_list_workout.xml")
                    val stream_file = FileInputStream(file)
                    val parser = XMLDOMParser(applicationContext)
                    val doc = parser.getDocument(stream_file)

                    if (file.length() != 0L) {
                        val nodeList = doc!!.getElementsByTagName("workout")

                        val finalWorkouts = parser.getXMLWorkoutValue(nodeList)
                        val workout = finalWorkouts[indice_workout]
                        workout.list_etape[indice_key] = etap_tmp
                        finalWorkouts[indice_workout] = workout
                        parser.writeToSDCardWorkoutXML("", finalWorkouts)


                        val train_adap =
                            TrainingAdapter(applicationContext, workout.list_etape)

                        listView_show_training.adapter = train_adap
                        listView_show_training.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                if (isUp) {
                                    if (rel_layout_edit_train != null) {
                                        if (Build.VERSION.SDK_INT >= 21) {
                                            if (rel_layout_edit_train != null) {
                                                rel_layout_edit_train.translationY = 200f
                                                isUp = false
                                            }
                                        }
                                    }
                                }
                                val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_LONG)
                                textView_go.visibility = View.GONE
                                view.setOnClickListener(null)
                                val pos = id.toInt()
                                if (time_etape_view != null) {
                                    if (listView_show_training != null) {
                                        listView_show_training.isEnabled = false
                                    }
                                    toast.setText("GO")
                                    toast.show()
                                    val timeEtapeView = time_etape_view as TimerView

                                    val tmrEtpAnim = TimerEtapeAnimation(timeEtapeView, 360)
                                    tmrEtpAnim.duration =
                                        (workout.list_etape[position].temps * 1000).toLong()
                                    tmrEtpAnim.setAnimationListener(object : Animation.AnimationListener {
                                        override fun onAnimationStart(animation: Animation) {}

                                        override fun onAnimationEnd(animation: Animation) {
                                            timeEtapeView.paint.color = Color.rgb(254, 195, 1)
                                            toast.setText("Now rest time")
                                            toast.show()
                                            timeEtapeView.clearAnimation()
                                            timeEtapeView.angle = 0f
                                            val tmrEtpAnim2 =
                                                TimerEtapeAnimation(timeEtapeView, 360)
                                            tmrEtpAnim2.duration =
                                                (workout.list_etape[pos].pause * 1000).toLong()
                                            tmrEtpAnim2.setAnimationListener(object : Animation.AnimationListener {
                                                override fun onAnimationStart(animation: Animation) {}

                                                override fun onAnimationEnd(animation: Animation) {
                                                    if (listView_show_training != null) {
                                                        listView_show_training.isEnabled = true
                                                    }
                                                    timeEtapeView.paint.color = Color.rgb(19, 142, 0)
                                                    toast.setText("Done")
                                                    toast.show()
                                                    timeEtapeView.clearAnimation()
                                                    timeEtapeView.angle = 0f
                                                    val tmrEtpAnim3 =
                                                        TimerEtapeAnimation(timeEtapeView, 360)
                                                    tmrEtpAnim3.duration = 200
                                                    timeEtapeView.startAnimation(tmrEtpAnim3)

                                                }

                                                override fun onAnimationRepeat(animation: Animation) {}
                                            })

                                            timeEtapeView.startAnimation(tmrEtpAnim2)
                                        }

                                        override fun onAnimationRepeat(animation: Animation) {}
                                    })

                                    timeEtapeView.startAnimation(tmrEtpAnim)

                                }
                            }
                        listView_show_training.onItemLongClickListener =
                            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                                if (Build.VERSION.SDK_INT >= 21) {
                                    if (rel_layout_edit_train != null) {
                                        rel_layout_edit_train.translationY = -200f
                                        isUp = true
                                    }
                                }
                                testTmp = id.toInt()
                                true
                            }
                    }
                }
                super.onActivityResult(requestCode, resultCode, intent)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun buttonPressed(view: View) {
        when (view.id) {
            R.id.button_back_showtrain -> {
                if (listView_show_training != null) {
                    if (!listView_show_training.isEnabled)
                        listView_show_training.isEnabled = true
                }
                finish()
            }

            R.id.button_back_transit -> {
                    if (Build.VERSION.SDK_INT >= 21) {
                        rel_layout_edit_train.translationY = 200f
                        isUp = false
                    }
            }

            R.id.button_edit_transit -> {
                if (rel_layout_edit_train != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rel_layout_edit_train != null) {
                            rel_layout_edit_train.translationY = 200f
                            isUp = false
                        }
                    }
                }
                val intent = Intent(this@ShowTrainingActivity, EditEtapeActivity::class.java)
                intent.putExtra(EXTRA_POSITION_KEY, "" + testTmp)
                intent.putExtra(EXTRA_TYPE_ACTIVITY, "transition")
                intent.putExtra(EXTRA_POSITION_CHOICE, getIntent().getStringExtra(EXTRA_POSITION_CHOICE))
                startActivityForResult(intent, 1)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ShowTrainingActivity::class.java)
        }
    }

}
