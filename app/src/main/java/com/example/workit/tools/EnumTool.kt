package com.example.workit.tools

import android.os.Environment

/**
 * Created by JustinRudat on 12/03/2019.
 */

class EnumTool {
    object STORAGE_PATH {
        val internal = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
        val shortlocalpath = "/Workout/total_list_workout.xml"
    }
}