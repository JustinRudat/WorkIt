package com.example.workit.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.example.workit.R
import com.example.workit.adapter.WorkoutAdapter
import com.example.workit.tools.XMLDOMParser
import kotlinx.android.synthetic.main.content_showork.*
import kotlinx.android.synthetic.main.white_text_cell.*
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * Created by JustinRudat on 06/03/2019.
 */
class ShowWorkActivity : AppCompatActivity() {
    internal val EXTRA_POSITION_CHOICE = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showork)

        // parser le doc des workout
        val parser = XMLDOMParser(this)

        val stream_file: FileInputStream
        try {

            //stream = getResources().openRawResource(R.raw.total_list_workout);
            // pour ecrire sur sd

            val file = File(Environment.getExternalStorageDirectory().toString() + "/Workout/total_list_workout.xml")
            stream_file = FileInputStream(file)

            val doc = parser.getDocument(stream_file)

            if (file.length() != 0L) {
                val nodeList = doc!!.getElementsByTagName("workout")

                val final_workouts = parser.getXMLWorkoutValue(nodeList)
                Collections.sort(
                    final_workouts
                ) { o1, o2 -> o1.toString().compareTo(o2.toString()) }

                val adapter = WorkoutAdapter(this@ShowWorkActivity, final_workouts)
                if (workoutListView != null) {
                    workoutListView.onItemClickListener = AdapterView.OnItemClickListener(fun(
                        arg0: AdapterView<*>,
                        arg1: View,
                        position: Int,
                        arg3: Long
                    ) {
                        val intent = Intent(this@ShowWorkActivity, ShowTrainingActivity::class.java)
                        val str_tmp = "" + arg3
                        intent.putExtra(EXTRA_POSITION_CHOICE, str_tmp)
                        startActivity(intent)
                    })

                    workoutListView.onItemLongClickListener =
                        AdapterView.OnItemLongClickListener { parent, view, position, id ->
                            if (imageButton_delete != null) {
                                if (imageButton_delete.visibility == View.INVISIBLE) {
                                    imageButton_delete.visibility = View.VISIBLE
                            } else {
                                    imageButton_delete.visibility = View.INVISIBLE
                            }
                        }
                        val str_tmp = "" + id
                        intent.putExtra(EXTRA_POSITION_CHOICE, str_tmp)
                        true
                    }
                    workoutListView.adapter = adapter

                }
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        // fin parseur


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
        when (view) {
            button_back_showork -> finish()
        }
    }



    fun buttonLongPressed(v: View) {
        when (v) {
            imageButton_delete -> {
                val parser = XMLDOMParser(applicationContext)

                println(intent.getStringExtra(EXTRA_POSITION_CHOICE))

                val array_wo_tmp =
                    parser.deleteWorkoutByNumber(Integer.parseInt(intent.getStringExtra(EXTRA_POSITION_CHOICE)), v)
                val wo_adapt_tmp = workoutListView.adapter as WorkoutAdapter
                wo_adapt_tmp.clear()
                wo_adapt_tmp.addAll(array_wo_tmp!!)
                wo_adapt_tmp.notifyDataSetChanged()

                imageButton_delete.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ShowWorkActivity::class.java)
        }
    }
}

