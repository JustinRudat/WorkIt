package com.example.workit.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.workit.R
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Created by JustinRudat on 06/03/2019.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkStoragePermission()
        val f1 = File(Environment.getExternalStorageDirectory().toString() + File.separator + "Workout")
        f1.mkdirs()
        val file = File(Environment.getExternalStorageDirectory().toString(), "/Workout/total_list_workout.xml")
        try {
            val tryTest = file.createNewFile()
            if (tryTest) {
                val fileOuput = FileWriter(file)
                fileOuput.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                fileOuput.write(
                    "<workouts><workout name_workout=\"le Nom\" nb_etape=\"3\">\n" +
                            "        <etape nom_eta=\"Etapeux\" desc_eta=\"ne rien faire\" temps=\"30\" pause=\"45\"></etape>\n" +
                            "        <etape nom_eta=\"Etapeux2\" desc_eta=\"ne rien faire\" temps=\"30\" pause=\"45\"></etape>\n" +
                            "        <etape nom_eta=\"Etapeux3\" desc_eta=\"ne rien faire\" temps=\"30\" pause=\"45\"></etape>\n" +
                            "    </workout></workouts>"
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    private fun checkStoragePermission(): Boolean {
        val tag = ""
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(tag, "Permission is granted")
                return true
            } else {

                Log.v(tag, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                return false
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(tag, "Permission is granted")
            return true
        }
    }


    fun buttonPressed(view: View) {
        print("HEY")
        when (view) {
            showWorkout -> {
                val intent = ShowWorkActivity.newIntent(this@MainActivity)
                startActivity(intent)
            }
            addWorkout -> {
                print("YO")
                val intent = AddWorkoutActivity.newIntent(this@MainActivity)
                startActivity(intent)
            }
            button_quit -> finish()
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

}
