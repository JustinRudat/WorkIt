package com.example.workit.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
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
import com.example.workit.tools.XMLDOMParser
import kotlinx.android.synthetic.main.activity_addetape.*
import kotlinx.android.synthetic.main.content_addetape.*
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStream
import java.util.*

/**
 * Created by juju_ on 19/08/2016.
 */
abstract class AddEtapeActivity : AppCompatActivity() {
    internal val EXTRA_NOM_WORKOUT = "nom_du_workout"
    internal val EXTRA_NOMBRE_ETAPES = "nombre_d_etape"
    internal val EXTRA_NOM_ETAPE = "etape_string"
    internal val EXTRA_DESC_ETAPE = "desc_string"
    internal val EXTRA_TEMPS_ETAPE = "temps_string"
    internal val EXTRA_PAUSE_ETAPE = "pause_string"
    internal val EXTRA_TYPE_ACTIVITY = "activity_type"
    internal val EXTRA_POSITION_KEY = "key_position"
    var array_work: ArrayList<Workout>? = null
    abstract var workout: Workout
    internal abstract var array_etape_tmp: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addetape)


        val intent = intent
        val textView1_add_etape = textView1_add_etape
        val textView2_add_etape = textView2_add_etape

        if (intent != null) {
            textView1_add_etape.text = intent.getStringExtra(EXTRA_NOM_WORKOUT)
            textView2_add_etape.text = intent.getStringExtra(EXTRA_NOMBRE_ETAPES) + " exercices."
            workout = Workout(
                Integer.parseInt(intent.getStringExtra(EXTRA_NOMBRE_ETAPES)),
                intent.getStringExtra(EXTRA_NOM_WORKOUT)
            )
            for (i in 0 until workout.nbEtape) {
                workout.list_etape.add(Etape("New Exercice", 0, 0))
            }
            array_etape_tmp = ArrayList(workout.nbEtape)
            for (i in 0 until workout.nbEtape) {
                array_etape_tmp.add("New Exercice")
            }

            val array_adapt_tmp =
                ArrayAdapter(this@AddEtapeActivity, android.R.layout.simple_list_item_1, array_etape_tmp)
            listView_add_etape.adapter = array_adapt_tmp
            listView_add_etape.setOnItemClickListener(
                fun(arg0: AdapterView<*>, arg1: View, position: Int, arg3: Long) {
                    val intent_tmp = Intent(this@AddEtapeActivity, EditEtapeActivity::class.java)
                    intent_tmp.putExtra(EXTRA_TYPE_ACTIVITY, "Add_etape")
                    intent_tmp.putExtra(EXTRA_POSITION_KEY, "" + arg3)
                    startActivityForResult(intent_tmp, 1)

                }
            )
        }
        setSupportActionBar(toolbar)


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


                array_etape_tmp[workout.list_etape.indexOf(etap_tmp)] = etap_tmp.titre

                val array_adapt_tmp =
                    ArrayAdapter(this@AddEtapeActivity, android.R.layout.simple_list_item_1, array_etape_tmp)
                listView_add_etape.adapter = array_adapt_tmp

                listView_add_etape.setOnItemClickListener(
                    fun(arg0: AdapterView<*>, arg1: View, position: Int, arg3: Long) {
                        val intent_tmp = Intent(this@AddEtapeActivity, EditEtapeActivity::class.java)
                        intent_tmp.putExtra(EXTRA_TYPE_ACTIVITY, "Add_etape")
                        intent_tmp.putExtra(EXTRA_POSITION_KEY, "" + arg3)
                        startActivityForResult(intent_tmp, 1)
                    })
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    fun buttonPressed(view: View) {
        when (view.id) {
            R.id.button_workout_add -> {
                val parser = XMLDOMParser(this)
                val manager = assets
                val stream: InputStream
                val stream_file: FileInputStream
                var writer: FileWriter? = null
                try {

                    //stream = getResources().openRawResource(R.raw.total_list_workout);
                    // pour ecrire sur sd

                    val file =
                        File(Environment.getExternalStorageDirectory().toString(), "/Workout/total_list_workout.xml")
                    if (file.length() == 0L) {

                        writer = FileWriter(file)
                        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
                        writer.write("<workouts>\n<workout name_workout=\"" + workout.toString() + "\" nb_etape=\"" + workout.nbEtape + "\">\n")
                        for (etp_tmp in workout.list_etape) {
                            writer.write("<etape nom_eta=\"" + etp_tmp.titre + "\" desc_eta=\"" + etp_tmp.desc + "\" temps=\"" + etp_tmp.temps + "\" pause=\"" + etp_tmp.pause + "\"></etape>\n")
                        }
                        writer.write("</workout>\n</workouts>\n")
                        writer.flush()

                    } else {
                        stream_file = FileInputStream(file)

                        val doc = parser.getDocument(stream_file)


                        val nodeList = doc!!.getElementsByTagName("workout")
                        val final_workouts = parser.getXMLWorkoutValue(nodeList)
                        final_workouts.add(workout)

                        parser.writeToSDCardWorkoutXML(
                            Environment.getExternalStorageDirectory().toString() + "Workout/total_list_workout.xml",
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

            R.id.button_back_addetp -> finish()
        }
    }
}