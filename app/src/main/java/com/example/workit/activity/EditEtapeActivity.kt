package com.example.workit.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.workit.R
import com.example.workit.data.Etape
import kotlinx.android.synthetic.main.content_editetape.*

/**
 * Created by JustinRudat on 06/03/2019.
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

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val blockCharacterSet = "&~#^|$%*!@/()'\"\\:;,?{}=!$^';,?×><÷{}€£¥₩%~`¤♡♥|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
            if (source != null && blockCharacterSet.contains("" + source)) {
                ""
            } else null
        }
        val input_tab = arrayOf(filter)
        edit_etape_nom.filters = input_tab
        edit_etape_desc.filters = input_tab
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
                val intent: Intent
                if (getIntent().getStringExtra(EXTRA_TYPE_ACTIVITY) == "transition") {

                    intent = ShowTrainingActivity.newIntent(this@EditEtapeActivity)
                } else {
                    intent = AddEtapeActivity.newIntent(this@EditEtapeActivity)
                }
                val toast = Toast.makeText(applicationContext, "Data missing, please check again", Toast.LENGTH_LONG)

                if (edit_etape_nom == null || edit_etape_temps == null || edit_etape_pause == null) {
                    toast.setText("Data missing, please check again")
                    toast.show()
                } else {
                    if (edit_etape_nom.text.toString() == "") {
                        toast.setText("Exercice name is missing")
                        toast.show()
                    } else {
                        if (edit_etape_temps.text.toString() == "") {
                            toast.setText("Timer is missing.\n If you don\'t want it, please set it to 0")
                            toast.show()
                        } else {
                            if (edit_etape_pause.text.toString() == "") {
                                toast.setText("Pause is missing.\n" + " If you don't want it, please set it to 0")
                                toast.show()
                            } else {
                                intent.putExtra(EXTRA_NOM_ETAPE, edit_etape_nom.text.toString())
                                if (edit_etape_desc == null) {
                                    intent.putExtra(EXTRA_DESC_ETAPE, "")
                                } else {
                                    intent.putExtra(EXTRA_DESC_ETAPE, edit_etape_desc.text.toString())
                                }
                                intent.putExtra(EXTRA_TEMPS_ETAPE, edit_etape_temps.text.toString())
                                intent.putExtra(EXTRA_PAUSE_ETAPE, edit_etape_pause.text.toString())
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
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, EditEtapeActivity::class.java)
        }
    }

}

