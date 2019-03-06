package com.example.workit.data

/**
 * Created by juju_ on 19/08/2016.
 */
class Etape {
    var titre: String
    var desc = ""
    var temps: Int = 0
    var pause: Int = 0

    constructor(titre: String, temps: Int, pause: Int) {
        this.titre = titre
        this.pause = pause
        this.temps = temps
    }

    constructor(titre: String, desc: String, temps: Int, pause: Int) {
        this.titre = titre
        this.pause = pause
        this.temps = temps
        this.desc = desc
    }

    fun etape2XML(): String {
        return "<etape titre=$titre desc=$desc temps=$temps pause=$pause></etape>"
    }


}
