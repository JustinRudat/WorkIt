package com.example.workit.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.example.workit.R
import com.example.workit.adapter.TrainingAdapter
import com.example.workit.data.Etape
import com.example.workit.tools.TimerEtapeAnimation
import com.example.workit.tools.TimerView
import com.example.workit.tools.XMLDOMParser

import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Created by juju_ on 22/08/2016.
 */
class ShowTrainingActivity : AppCompatActivity() {
    internal val EXTRA_POSITION_CHOICE = "0"
    internal val EXTRA_NOM_ETAPE = "etape_string"
    internal val EXTRA_DESC_ETAPE = "desc_string"
    internal val EXTRA_TEMPS_ETAPE = "temps_string"
    internal val EXTRA_PAUSE_ETAPE = "pause_string"
    internal val EXTRA_POSITION_KEY = "key_position"
    internal val EXTRA_TYPE_ACTIVITY = "activity_type"

    var test_tmp = 0
    var isUp = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_training)
        val rl_toto = findViewById<View>(R.id.total_view_show_train) as RelativeLayout
        rl_toto?.setOnClickListener {
            if (isUp) {
                val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                if (rl_tmp != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rl_tmp != null) {
                            rl_tmp.translationY = 200f
                            isUp = false
                        }
                    }
                }
            }
        }
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val listV = findViewById<View>(R.id.listView_show_training) as ListView
        val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
        if (Build.VERSION.SDK_INT >= 21) {
            if (rl_tmp != null) {
                rl_tmp.z = 5f
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
            val str_pos = intent.getStringExtra(EXTRA_POSITION_CHOICE)

            val position = Integer.parseInt(str_pos)

            val nodeList = doc!!.getElementsByTagName("workout")

            val final_workouts = parser.getXMLWorkoutValue(nodeList)
            val wo_tmp = final_workouts[position]
            val txtV_nom = findViewById<View>(R.id.textView_lrg_training_name) as TextView
            if (txtV_nom != null) {
                txtV_nom.text = wo_tmp.toString()
            }

            val adapter = TrainingAdapter(this@ShowTrainingActivity, wo_tmp.list_etape)
            if (listV != null) {
                listV.adapter = adapter


                listV.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_LONG)
                    if (isUp) {
                        val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                        if (rl_tmp != null) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                if (rl_tmp != null) {
                                    rl_tmp.translationY = 200f
                                    isUp = false
                                }
                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 16) {
                        //Permet de ne pas recycler la vue de l'animation en cours d'execution(animation timer multiple)

                        view.setHasTransientState(true)


                    }
                    listV.isEnabled = false

                    //listV.setScrollContainer(false);
                    view.findViewById<View>(R.id.textView_go).visibility = View.GONE
                    view.setOnClickListener(null)
                    val pos = id.toInt()
                    val tmr_view = view.findViewById<View>(R.id.time_etape_view) as TimerView


                    if (tmr_view != null) {
                        toast.setText("GO")
                        toast.show()

                        val tmr_etp_anim = TimerEtapeAnimation(tmr_view, 360)
                        tmr_etp_anim.duration = (wo_tmp.list_etape[pos].temps * 1000).toLong()
                        tmr_etp_anim.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {

                            }

                            override fun onAnimationEnd(animation: Animation) {
                                tmr_view.setPaint(Color.rgb(254, 195, 1))
                                toast.setText("Now rest time")
                                toast.show()
                                tmr_view.clearAnimation()
                                tmr_view.angle = 0f
                                val tmr_etp_anim_2 = TimerEtapeAnimation(tmr_view, 360)
                                tmr_etp_anim_2.duration = (wo_tmp.list_etape[pos].pause * 1000).toLong()
                                tmr_etp_anim_2.setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation) {}

                                    override fun onAnimationEnd(animation: Animation) {
                                        tmr_view.setPaint(Color.rgb(19, 142, 0))

                                        toast.setText("Done")
                                        toast.show()
                                        tmr_view.clearAnimation()
                                        tmr_view.angle = 0f
                                        val tmr_etp_anim_3 = TimerEtapeAnimation(tmr_view, 360)
                                        tmr_etp_anim_3.duration = 200

                                        tmr_view.startAnimation(tmr_etp_anim_3)
                                        if (Build.VERSION.SDK_INT >= 16) {
                                            view.setHasTransientState(true)
                                        }
                                        listV.isEnabled = true

                                    }

                                    override fun onAnimationRepeat(animation: Animation) {}
                                })

                                tmr_view.startAnimation(tmr_etp_anim_2)
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })

                        tmr_view.startAnimation(tmr_etp_anim)

                    }
                }
                listV.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
                    val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rl_tmp != null) {

                            rl_tmp.translationY = -200f
                            isUp = true
                        }
                    }
                    test_tmp = id.toInt()
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
            val lst_view_tmp = findViewById<View>(R.id.listView_show_training) as ListView
            if (resultCode == 1) {
                val name_etape_tmp: String?
                val desc_etape_tmp: String
                val temps_etape_tmp: Int
                val pause_etape_tmp: Int
                val rl_toto = findViewById<View>(R.id.total_view_show_train) as RelativeLayout
                rl_toto?.setOnClickListener {
                    if (isUp) {
                        val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                        if (rl_tmp != null) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                if (rl_tmp != null) {
                                    rl_tmp.translationY = 200f
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

                        val final_workouts = parser.getXMLWorkoutValue(nodeList)
                        val workout = final_workouts[indice_workout]
                        workout.list_etape[indice_key] = etap_tmp
                        final_workouts[indice_workout] = workout
                        parser.writeToSDCardWorkoutXML("", final_workouts)


                        val train_adap =
                            TrainingAdapter(applicationContext, workout.list_etape)

                        val lstview = findViewById<View>(R.id.listView_add_etape) as ListView
                        lst_view_tmp.adapter = train_adap
                        lst_view_tmp.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                if (isUp) {
                                    val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                                    if (rl_tmp != null) {
                                        if (Build.VERSION.SDK_INT >= 21) {
                                            if (rl_tmp != null) {
                                                rl_tmp.translationY = 200f
                                                isUp = false
                                            }
                                        }
                                    }
                                }
                                val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_LONG)
                                view.findViewById<View>(R.id.textView_go).visibility = View.GONE
                                view.setOnClickListener(null)
                                val pos = id.toInt()
                                val tmr_view = view.findViewById<View>(R.id.time_etape_view) as TimerView
                                if (tmr_view != null) {
                                    val lst_view_tmp = findViewById<View>(R.id.listView_show_training) as ListView
                                    if (lst_view_tmp != null) {
                                        lst_view_tmp.isEnabled = false
                                    }
                                    toast.setText("GO")
                                    toast.show()

                                    val tmr_etp_anim = TimerEtapeAnimation(tmr_view, 360)
                                    tmr_etp_anim.duration =
                                        (workout.list_etape[position].temps * 1000).toLong()
                                    tmr_etp_anim.setAnimationListener(object : Animation.AnimationListener {
                                        override fun onAnimationStart(animation: Animation) {}

                                        override fun onAnimationEnd(animation: Animation) {
                                            tmr_view.setPaint(Color.rgb(254, 195, 1))
                                            toast.setText("Now rest time")
                                            toast.show()
                                            tmr_view.clearAnimation()
                                            tmr_view.angle = 0f
                                            val tmr_etp_anim_2 =
                                                TimerEtapeAnimation(tmr_view, 360)
                                            tmr_etp_anim_2.duration =
                                                (workout.list_etape[pos].pause * 1000).toLong()
                                            tmr_etp_anim_2.setAnimationListener(object : Animation.AnimationListener {
                                                override fun onAnimationStart(animation: Animation) {}

                                                override fun onAnimationEnd(animation: Animation) {
                                                    val lst_view_tmp =
                                                        findViewById<View>(R.id.listView_show_training) as ListView
                                                    if (lst_view_tmp != null) {
                                                        lst_view_tmp.isEnabled = true
                                                    }
                                                    tmr_view.setPaint(Color.rgb(19, 142, 0))
                                                    toast.setText("Done")
                                                    toast.show()
                                                    tmr_view.clearAnimation()
                                                    tmr_view.angle = 0f
                                                    val tmr_etp_anim_3 =
                                                        TimerEtapeAnimation(tmr_view, 360)
                                                    tmr_etp_anim_3.duration = 200
                                                    tmr_view.startAnimation(tmr_etp_anim_3)

                                                }

                                                override fun onAnimationRepeat(animation: Animation) {}
                                            })

                                            tmr_view.startAnimation(tmr_etp_anim_2)
                                        }

                                        override fun onAnimationRepeat(animation: Animation) {}
                                    })

                                    tmr_view.startAnimation(tmr_etp_anim)

                                }
                            }
                        lst_view_tmp.onItemLongClickListener =
                            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                                val rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                                if (Build.VERSION.SDK_INT >= 21) {
                                    if (rl_tmp != null) {
                                        rl_tmp.translationY = -200f
                                        isUp = true
                                    }
                                }
                                test_tmp = id.toInt()
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
        val rl_tmp: RelativeLayout?
        when (view.id) {
            R.id.button_back_showtrain -> {
                val lst_tmp = findViewById<View>(R.id.listView_show_training) as ListView
                if (lst_tmp != null) {
                    if (!lst_tmp.isEnabled)
                        lst_tmp.isEnabled = true
                }
                finish()
            }

            R.id.button_back_transit -> {
                rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                if (rl_tmp != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rl_tmp != null) {
                            rl_tmp.translationY = 200f
                            isUp = false
                        }
                    }

                }
            }

            R.id.button_edit_transit -> {
                rl_tmp = findViewById<View>(R.id.rel_layout_edit_train) as RelativeLayout
                if (rl_tmp != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        if (rl_tmp != null) {
                            rl_tmp.translationY = 200f
                            isUp = false
                        }
                    }
                }
                val intent = Intent(this@ShowTrainingActivity, EditEtapeActivity::class.java)
                intent.putExtra(EXTRA_POSITION_KEY, "" + test_tmp)
                intent.putExtra(EXTRA_TYPE_ACTIVITY, "transition")
                intent.putExtra(EXTRA_POSITION_CHOICE, getIntent().getStringExtra(EXTRA_POSITION_CHOICE))
                startActivityForResult(intent, 1)
            }
        }
    }

}
