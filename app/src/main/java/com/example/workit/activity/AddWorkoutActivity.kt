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

/**
 * Created by juju_ on 19/08/2016.
 */
class AddWorkoutActivity : AppCompatActivity() {
    internal val EXTRA_NOM_WORKOUT = "nom_du_workout"
    internal val EXTRA_NOMBRE_ETAPES = "nombre_d_etape"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addworkout)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val le_name_workout = findViewById<View>(R.id.new_training_name) as EditText
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val blockCharacterSet = "&~#^|$%*!@/()'\"\\:;,?{}<>=!$^';,?×÷{}€£¥₩%~`¤♡♥|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
            if (source != null && blockCharacterSet.contains("" + source)) {
                ""
            } else null
        }
        le_name_workout.filters = arrayOf(filter)

    }

    override fun onRestart() {
        super.onRestart()
        finish()
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
            R.id.button_back_addwork -> finish()

            R.id.add_etape_workout_button -> {
                val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
                val nom_workout = findViewById<View>(R.id.new_training_name) as EditText
                val nombre_etape = findViewById<View>(R.id.nombre_d_etape) as EditText
                if (nom_workout != null && nombre_etape != null) {
                    if (nom_workout.text.toString() == "") {
                        toast.setText("Name missing")
                        toast.show()
                    } else
                        if (nombre_etape.text.toString() == "" || Integer.parseInt(nombre_etape.text.toString()) == 0) {
                            toast.setText("No exercices for a training ?\n Are you sure about that ?")
                            toast.show()
                        } else {
                            val intent = Intent(this@AddWorkoutActivity, AddEtapeActivity::class.java)
                            intent.putExtra(EXTRA_NOM_WORKOUT, nom_workout.text.toString())
                            intent.putExtra(EXTRA_NOMBRE_ETAPES, nombre_etape.text.toString())
                            startActivity(intent)
                        }
                } else {
                    toast.setText("Data missing, please check again")
                    toast.show()
                }
            }
        }
    }
}
