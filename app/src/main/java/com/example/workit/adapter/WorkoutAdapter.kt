package com.example.workit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.workit.R
import com.example.workit.data.Workout

/**
 * Created by Created by JustinRudat on 06/03/2019.
 */
class WorkoutAdapter
    (context: Context, list_workout: List<Workout>) :
    ArrayAdapter<Workout>(context, R.layout.white_text_cell, list_workout) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewTmp = convertView
        if (convertViewTmp == null) {
            convertViewTmp = LayoutInflater.from(context).inflate(R.layout.white_text_cell, parent, false)
        }
        val woTmp = getItem(position)

        val viewHolder = WorkoutViewHolder()

        viewHolder.nomWorkout = convertViewTmp!!.findViewById<View>(R.id.textView_nom_showork) as TextView
        viewHolder.nbEtpWorkout = convertViewTmp.findViewById<View>(R.id.textView_nb_etp_showork) as TextView
        viewHolder.nomWorkout!!.text = woTmp!!.toString()
        viewHolder.nbEtpWorkout!!.text = "" + woTmp.nbEtape

        convertViewTmp.tag = viewHolder

        return convertViewTmp
    }

    private inner class WorkoutViewHolder {
        var nomWorkout: TextView? = null
        var nbEtpWorkout: TextView? = null


    }
}
