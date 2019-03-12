package com.example.workit.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.workit.R
import kotlinx.android.synthetic.main.content_editetape.*

/**
 * Created by JustinRudat on 06/03/2019.
 */
class EditEtapeActivity : EditActivity() {
    private val EXTRA_NOM_ETAPE = "etape_string"
    private val EXTRA_DESC_ETAPE = "desc_string"
    private val EXTRA_TEMPS_ETAPE = "temps_string"
    private val EXTRA_PAUSE_ETAPE = "pause_string"
    private val EXTRA_POSITION_KEY = "key_position"
    private val EXTRA_TYPE_ACTIVITY = "activity_type"
    private val EXTRA_POSITION_CHOICE = "0"

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
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun buttonPressed(view: View) {
        when (view) {
            button_etape_add -> {
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

