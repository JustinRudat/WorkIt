package com.example.workit.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import com.example.workit.R
import com.example.workit.adapter.WorkoutAdapter
import com.example.workit.tools.XMLDOMParser
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * Created by juju_ on 19/08/2016.
 */
class ShowWorkActivity : AppCompatActivity() {
    internal val EXTRA_POSITION_CHOICE = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showork)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val listV = findViewById<View>(R.id.workoutListView) as ListView

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
                if (listV != null) {
                    listV.onItemClickListener = AdapterView.OnItemClickListener(fun(
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

                    listV.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
                        val img_buton_tmp = view.findViewById<View>(R.id.imageButton_delete) as ImageButton
                        if (img_buton_tmp != null) {
                            if (img_buton_tmp.visibility == View.INVISIBLE) {
                                img_buton_tmp.visibility = View.VISIBLE
                            } else {
                                img_buton_tmp.visibility = View.INVISIBLE
                            }
                        }
                        val str_tmp = "" + id
                        intent.putExtra(EXTRA_POSITION_CHOICE, str_tmp)
                        true
                    }
                    listV.adapter = adapter

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
        when (view.id) {
            R.id.button_back_showork -> finish()
        }
    }


    //TODO
    fun buttonLongPressed(v: View) {
        when (v.id) {
            R.id.imageButton_delete -> {
                val parser = XMLDOMParser(applicationContext)

                println(intent.getStringExtra(EXTRA_POSITION_CHOICE))

                val array_wo_tmp =
                    parser.deleteWorkoutByNumber(Integer.parseInt(intent.getStringExtra(EXTRA_POSITION_CHOICE)), v)
                val lv_tmp = findViewById<View>(R.id.workoutListView) as ListView
                val wo_adapt_tmp = lv_tmp.adapter as WorkoutAdapter
                wo_adapt_tmp.clear()
                wo_adapt_tmp.addAll(array_wo_tmp!!)
                wo_adapt_tmp.notifyDataSetChanged()

                val img_but_del_tmp = v.findViewById<View>(R.id.imageButton_delete) as ImageButton
                img_but_del_tmp.visibility = View.INVISIBLE
            }
        }
    }
}
