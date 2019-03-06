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
 * Created by juju_ on 23/08/2016.
 */
class WorkoutAdapter//tweets est la liste des models à afficher
    (context: Context, list_workout: List<Workout>) :
    ArrayAdapter<Workout>(context, R.layout.white_text_cell, list_workout) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.white_text_cell, parent, false)
        }
        val wo_tmp = getItem(position)

        val viewHolder = WorkoutViewHolder()

        viewHolder.nom_workout = convertView!!.findViewById<View>(R.id.textView_nom_showork) as TextView
        viewHolder.nb_etp_workout = convertView.findViewById<View>(R.id.textView_nb_etp_showork) as TextView
        viewHolder.nom_workout!!.text = wo_tmp!!.toString()
        viewHolder.nb_etp_workout!!.text = "" + wo_tmp.nbEtape

        convertView.tag = viewHolder

        return convertView
    }

    private inner class WorkoutViewHolder {
        var nom_workout: TextView? = null
        var nb_etp_workout: TextView? = null


    }
}
