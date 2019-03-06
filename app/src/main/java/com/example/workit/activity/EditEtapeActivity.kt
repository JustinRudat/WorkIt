package com.example.workit.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.workit.R
import com.example.workit.data.Etape

/**
 * Created by juju_ on 22/08/2016.
 */
class EditEtapeActivity : AppCompatActivity() {
    internal val EXTRA_NOM_ETAPE = "etape_string"
    internal val EXTRA_DESC_ETAPE = "desc_string"
    internal val EXTRA_TEMPS_ETAPE = "temps_string"
    internal val EXTRA_PAUSE_ETAPE = "pause_string"
    internal val EXTRA_POSITION_KEY = "key_position"
    internal val EXTRA_TYPE_ACTIVITY = "activity_type"
    internal val EXTRA_POSITION_CHOICE = "0"


    var etape_tmp: Etape? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editetape)

        val intent = intent

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val le_name_exercice = findViewById<View>(R.id.edit_etape_nom) as EditText
        val le_desc_exercice = findViewById<View>(R.id.edit_etape_desc) as EditText
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val blockCharacterSet = "&~#^|$%*!@/()'\"\\:;,?{}=!$^';,?×><÷{}€£¥₩%~`¤♡♥|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
            if (source != null && blockCharacterSet.contains("" + source)) {
                ""
            } else null
        }
        val input_tab = arrayOf(filter)
        le_name_exercice.filters = input_tab
        le_desc_exercice.filters = input_tab
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
            R.id.button_back_edit -> finish()

            R.id.button_etape_add -> {
                val position_key = Integer.parseInt(intent.getStringExtra(EXTRA_POSITION_KEY))
                val position_workout = intent.getStringExtra(EXTRA_POSITION_CHOICE)
                val titre_tmp = findViewById<View>(R.id.edit_etape_nom) as EditText
                val desc_tmp = findViewById<View>(R.id.edit_etape_desc) as EditText
                val temps_tmp = findViewById<View>(R.id.edit_etape_temps) as EditText
                val pause_tmp = findViewById<View>(R.id.edit_etape_pause) as EditText
                val intent: Intent
                if (getIntent().getStringExtra(EXTRA_TYPE_ACTIVITY) == "transition") {
                    intent = Intent(this@EditEtapeActivity, ShowTrainingActivity::class.java)
                } else {
                    intent = Intent(this@EditEtapeActivity, AddEtapeActivity::class.java)
                }
                val toast = Toast.makeText(applicationContext, "Data missing, please check again", Toast.LENGTH_LONG)

                if (titre_tmp == null || temps_tmp == null || pause_tmp == null) {
                    toast.setText("Data missing, please check again")
                    toast.show()
                } else {
                    if (titre_tmp.text.toString() == "") {
                        toast.setText("Exercice name is missing")
                        toast.show()
                    } else
                        if (temps_tmp.text.toString() == "") {
                            toast.setText("Timer is missing.\n If you don\'t want it, please set it to 0")
                            toast.show()
                        } else
                            if (pause_tmp.text.toString() == "") {
                                toast.setText("Pause is missing.\n" + " If you don't want it, please set it to 0")
                                toast.show()
                            } else {
                                intent.putExtra(EXTRA_NOM_ETAPE, titre_tmp.text.toString())
                                if (desc_tmp == null) {
                                    intent.putExtra(EXTRA_DESC_ETAPE, "")
                                } else {
                                    intent.putExtra(EXTRA_DESC_ETAPE, desc_tmp.text.toString())
                                }
                                intent.putExtra(EXTRA_TEMPS_ETAPE, temps_tmp.text.toString())
                                intent.putExtra(EXTRA_PAUSE_ETAPE, pause_tmp.text.toString())
                                intent.putExtra(EXTRA_POSITION_KEY, "" + position_key)
                                intent.putExtra(EXTRA_POSITION_CHOICE, position_workout)
                                setResult(1, intent)
                                finish()
                            }
                }
            }
        }
    }

}

