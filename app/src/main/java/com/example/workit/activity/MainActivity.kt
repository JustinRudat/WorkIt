package com.example.workit.activity

import android.Manifest
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkStoragePermission();
        setSupportActionBar(toolbar);
        val f1 = File(Environment.getExternalStorageDirectory().toString() + File.separator + "Workout")
        f1.mkdirs()
        //String str=Environment.getExternalStorageDirectory().toString()+File.separator+"Workout"+File.separator+"total_list_workout.xml";
        val file = File(Environment.getExternalStorageDirectory().toString(), "/Workout/total_list_workout.xml")
        try {
            val try_test = file.createNewFile()
            if (try_test) {
                var file_ouput: FileWriter;
                file_ouput = FileWriter(file)
                file_ouput.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                file_ouput.write(
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

    fun checkStoragePermission(): Boolean {
        var TAG = "";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                return true
            } else {

                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                return false
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            return true
        }
    }


    fun buttonPressed(view: View) {
        when (view.id) {
            R.id.showWorkout -> startActivity(Intent(this, ShowWorkActivity::class.java))
            R.id.addWorkout -> startActivity(Intent(this, AddWorkoutActivity::class.java))

            R.id.button_quit -> finish()
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
