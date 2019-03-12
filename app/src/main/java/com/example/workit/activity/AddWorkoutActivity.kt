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
import kotlinx.android.synthetic.main.content_addworkout.*

/**
 * Created by JustinRudat on 06/03/2019.
 */
class AddWorkoutActivity : EditActivity() {
    private val EXTRA_NOM_WORKOUT = "nom_du_workout"
    private val EXTRA_NOMBRE_ETAPES = "nombre_d_etape"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addworkout)
        val filter = InputFilter { source, _, _, _, _, _ ->
            val blockCharacterSet = "&~#^|$%*!@/()'\"\\:;,?{}<>=!$^';,?×÷{}€£¥₩%~`¤♡♥|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
            if (source != null && blockCharacterSet.contains("" + source)) {
                ""
            } else null
        }
        new_training_name.filters = arrayOf(filter)

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
        val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
        if (new_training_name != null && nombre_d_etape != null) {
            if (new_training_name.text.toString() == "") {
                toast.setText("Name missing")
                toast.show()
            } else
                if (nombre_d_etape.text.toString() == "" || Integer.parseInt(nombre_d_etape.text.toString()) == 0) {
                    toast.setText("No exercices for a training ?\n Are you sure about that ?")
                    toast.show()
                } else {
                    val intent = AddEtapeActivity.newIntent(this@AddWorkoutActivity)
                    intent.putExtra(EXTRA_NOM_WORKOUT, new_training_name.text.toString())
                    intent.putExtra(EXTRA_NOMBRE_ETAPES, nombre_d_etape.text.toString())
                    startActivity(intent)
                }
        } else {
            toast.setText("Data missing, please check again")
            toast.show()
        }


    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddWorkoutActivity::class.java)
        }
    }
}
