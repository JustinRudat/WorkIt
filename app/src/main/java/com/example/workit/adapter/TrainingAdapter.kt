package com.example.workit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.workit.R
import com.example.workit.data.Etape
import com.example.workit.tools.TimerEtapeAnimation

/**
 * Created by juju_ on 23/08/2016.
 */
class TrainingAdapter//tweets est la liste des models à afficher
    (context: Context, etape: List<Etape>) : ArrayAdapter<Etape>(context, R.layout.simple_textview_perso, etape) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.simple_textview_perso, parent, false)
        }
        val etape = getItem(position)

        val viewHolder = ExerciceViewHolder()

        viewHolder.nom_exercice = convertView!!.findViewById<View>(R.id.textView_name_etape) as TextView
        viewHolder.desc_exercice = convertView.findViewById<View>(R.id.textView_desc_etape) as TextView
        viewHolder.temps_exercice = convertView.findViewById<View>(R.id.textView_temps_etape) as TextView
        viewHolder.pause_exercice = convertView.findViewById<View>(R.id.textView_pause_etape) as TextView
        viewHolder.nom_exercice!!.setText(etape!!.titre)
        viewHolder.desc_exercice!!.setText(etape!!.desc)
        viewHolder.temps_exercice!!.text = "" + etape!!.temps
        viewHolder.pause_exercice!!.text = "" + etape.pause
        convertView.tag = viewHolder


        //il ne reste plus qu'à remplir notre vue


        return convertView
    }

    private inner class ExerciceViewHolder {
        var nom_exercice: TextView? = null
        var desc_exercice: TextView? = null
        var temps_exercice: TextView? = null
        var pause_exercice: TextView? = null
        var anim_circle: TimerEtapeAnimation? = null

    }
}
