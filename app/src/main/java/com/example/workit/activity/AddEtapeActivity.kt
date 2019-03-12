package com.example.workit.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.workit.R
import com.example.workit.data.Etape
import com.example.workit.data.Workout
import com.example.workit.tools.EnumTool.STORAGE_PATH.internal
import com.example.workit.tools.EnumTool.STORAGE_PATH.shortlocalpath
import com.example.workit.tools.XMLDOMParser
import kotlinx.android.synthetic.main.content_addetape.*
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

/**
 * Created by JustinRudat on 06/03/2019.
 */
class AddEtapeActivity : AppCompatActivity() {
    private val EXTRA_NOM_WORKOUT = "nom_du_workout"
    private val EXTRA_NOMBRE_ETAPES = "nombre_d_etape"
    private val EXTRA_NOM_ETAPE = "etape_string"
    private val EXTRA_DESC_ETAPE = "desc_string"
    private val EXTRA_TEMPS_ETAPE = "temps_string"
    private val EXTRA_PAUSE_ETAPE = "pause_string"
    private val EXTRA_TYPE_ACTIVITY = "activity_type"
    private val EXTRA_POSITION_KEY = "key_position"
    var workout: Workout = Workout(0, "default")
    private var arrayEtapeTmp: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addetape)

        if (intent != null) {
            textView1_add_etape.text = intent.getStringExtra(EXTRA_NOM_WORKOUT)
            // textView2_add_etape.text = intent.getStringExtra(EXTRA_NOMBRE_ETAPES) + " exercices."
            workout = Workout(
                Integer.parseInt(intent.getStringExtra(EXTRA_NOMBRE_ETAPES)),
                intent.getStringExtra(EXTRA_NOM_WORKOUT)
            )
            for (i in 0 until workout.nbEtape) {
                workout.list_etape.add(Etape("New Exercice", 0, 0))
            }
            arrayEtapeTmp = ArrayList(workout.nbEtape)
            for (i in 0 until workout.nbEtape) {
                arrayEtapeTmp.add("New Exercice")
            }

            val array_adapt_tmp =
                ArrayAdapter(this@AddEtapeActivity, android.R.layout.simple_list_item_1, arrayEtapeTmp)
            listView_add_etape.adapter = array_adapt_tmp
            listView_add_etape.setOnItemClickListener(
                fun(_: AdapterView<*>, _: View, _: Int, arg3: Long) {
                    val intentTmp = EditEtapeActivity.newIntent(this@AddEtapeActivity)
                    intentTmp.putExtra(EXTRA_TYPE_ACTIVITY, "Add_etape")
                    intentTmp.putExtra(EXTRA_POSITION_KEY, "" + arg3)
                    startActivityForResult(intentTmp, 1)

                }
            )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (resultCode == 1) {
            val name_etape_tmp: String?
            val desc_etape_tmp: String
            val temps_etape_tmp: Int
            val pause_etape_tmp: Int

            val etap_tmp: Etape

            if (intent != null) {
                name_etape_tmp = intent.extras!!.getString(EXTRA_NOM_ETAPE)
                desc_etape_tmp = intent.getStringExtra(EXTRA_DESC_ETAPE)
                temps_etape_tmp = Integer.parseInt(intent.getStringExtra(EXTRA_TEMPS_ETAPE))
                pause_etape_tmp = Integer.parseInt(intent.getStringExtra(EXTRA_PAUSE_ETAPE))
                val indice_key = Integer.parseInt(intent.getStringExtra(EXTRA_POSITION_KEY))
                etap_tmp =
                    Etape(name_etape_tmp, desc_etape_tmp, temps_etape_tmp, pause_etape_tmp)
                if (!workout.list_etape.contains(etap_tmp)) {
                    workout.list_etape[indice_key] = etap_tmp
                }


                arrayEtapeTmp[workout.list_etape.indexOf(etap_tmp)] = etap_tmp.titre

                val array_adapt_tmp =
                    ArrayAdapter(this@AddEtapeActivity, android.R.layout.simple_list_item_1, arrayEtapeTmp)
                listView_add_etape.adapter = array_adapt_tmp

                listView_add_etape.setOnItemClickListener(
                    fun(_: AdapterView<*>, _: View, _: Int, arg3: Long) {
                        val intentTmp = EditEtapeActivity.newIntent(this@AddEtapeActivity)
                        intentTmp.putExtra(EXTRA_TYPE_ACTIVITY, "Add_etape")
                        intentTmp.putExtra(EXTRA_POSITION_KEY, "" + arg3)
                        startActivityForResult(intentTmp, 1)
                    })
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    fun buttonPressed(view: View) {
        when (view) {
            button_workout_add -> {
                println(arrayEtapeTmp.size)
                val parser = XMLDOMParser(this)
                val streamFile: FileInputStream
                val writer: FileWriter?
                try {
                    val file =
                        File(internal, shortlocalpath)
                    if (file.length() == 0L) {

                        writer = FileWriter(file)
                        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
                        writer.write(
                            "<workouts>\n<workout name_workout=\""
                                    + workout.toString() + "\" nb_etape=\""
                                    + workout.nbEtape + "\">\n"
                        )
                        for (etp_tmp in workout.list_etape) {
                            writer.write(
                                "<etape nom_eta=\"" + etp_tmp.titre + "\" desc_eta=\""
                                        + etp_tmp.desc + "\" temps=\"" + etp_tmp.temps + "\" pause=\""
                                        + etp_tmp.pause + "\"></etape>\n"
                            )
                        }
                        writer.write("</workout>\n</workouts>\n")
                        writer.flush()

                    } else {
                        streamFile = FileInputStream(file)

                        val doc = parser.getDocument(streamFile)

                        val nodeList = doc!!.getElementsByTagName("workout")
                        val final_workouts = parser.getXMLWorkoutValue(nodeList)
                        final_workouts.add(workout)

                        parser.writeToSDCardWorkoutXML(
                            internal + shortlocalpath,
                            final_workouts
                        )
                    }
                    val toast = Toast.makeText(applicationContext, "$workout was succefully added.", Toast.LENGTH_LONG)
                    toast.show()
                    finish()

                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
                // fin parseur
            }

            button_back_addetp -> finish()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddEtapeActivity::class.java)
        }
    }
}
