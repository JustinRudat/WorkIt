package com.example.workit.data

import java.util.*

/**
 * Created by juju_ on 19/08/2016.
 */
class Workout(var nbEtape: Int, var nom: String) {
    var timer: Timer
    var list_etape: ArrayList<Etape>

    init {
        list_etape = ArrayList(nbEtape)
        timer = Timer()
    }

    fun workout2XML(): String {
        var toXML = "<workout name=$nom no_etape=$nbEtape>"
        for (etape in list_etape) {
            toXML += etape.etape2XML()
        }
        toXML += "</workout>"
        return toXML
    }

    fun addEtape(etape: Etape) {
        list_etape.add(etape)
    }

    override fun toString(): String {
        return nom
    }
}
