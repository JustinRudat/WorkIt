package com.example.workit.tools

/**
 * Created by JustinRudat on 06/03/2019.
 */

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View
import com.example.workit.data.Etape
import com.example.workit.data.Workout
import com.example.workit.tools.EnumTool.STORAGE_PATH.internal
import com.example.workit.tools.EnumTool.STORAGE_PATH.shortlocalpath
import org.w3c.dom.*
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.*
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class XMLDOMParser(var c: Context) {
    //Returns the entire XML document
    fun getDocument(inputStream: InputStream): Document? {
        var document: Document? = null
        val factory = DocumentBuilderFactory.newInstance()
        try {
            val db = factory.newDocumentBuilder()
            val inputSource = InputSource(inputStream)
            document = db.parse(inputSource)
        } catch (e: ParserConfigurationException) {
            Log.e("Error: ", e.message)
            return null
        } catch (e: SAXException) {
            Log.e("Error: ", e.message)
            return null
        } catch (e: IOException) {
            Log.e("Error: ", e.message)
            return null
        }

        return document
    }


    fun getXMLWorkoutValue(nodes: NodeList): ArrayList<Workout> {

        //

        val workouts = ArrayList<Workout>(nodes.length)

        var nodeNamedMap: NamedNodeMap
        var nodeNamedMapTmp: NamedNodeMap
        var children: NodeList
        var int_nb_etape: Int
        var int_temps_etape: Int
        var int_pause_etape: Int
        var str_nom_workout: String
        var str_titre_etape: String
        var str_desc_etape: String
        var node: Node
        var tmp_nod: Node
        var tmp_workout: Workout
        var tmp_etape: Etape

        for (i in 0 until nodes.length) {
            node = nodes.item(i)

            nodeNamedMap = node.attributes
            val tmp_attr1: Attr
            val tmp_attr2: Attr
            tmp_attr1 = nodeNamedMap.getNamedItem("nb_etape") as Attr
            tmp_attr2 = nodeNamedMap.getNamedItem("name_workout") as Attr

            int_nb_etape = Integer.parseInt(tmp_attr1.value)
            str_nom_workout = tmp_attr2.value

            tmp_workout = Workout(int_nb_etape, str_nom_workout)

            children = node.childNodes
            for (j in 0 until children.length) {
                if (j % 2 != 0) {
                    val tmp_attr3: Attr
                    val tmp_attr4: Attr
                    val tmp_attr5: Attr
                    val tmp_attr6: Attr
                    tmp_nod = children.item(j)
                    nodeNamedMapTmp = tmp_nod.attributes
                    //
                    //
                    tmp_attr3 = nodeNamedMapTmp.getNamedItem("nom_eta") as Attr
                    tmp_attr4 = nodeNamedMapTmp.getNamedItem("desc_eta") as Attr
                    tmp_attr5 = nodeNamedMapTmp.getNamedItem("temps") as Attr
                    tmp_attr6 = nodeNamedMapTmp.getNamedItem("pause") as Attr
                    str_titre_etape = tmp_attr3.value
                    str_desc_etape = tmp_attr4.value
                    int_temps_etape = Integer.parseInt(tmp_attr5.value)
                    int_pause_etape = Integer.parseInt(tmp_attr6.value)

                    tmp_etape =
                        Etape(str_titre_etape, str_desc_etape, int_temps_etape, int_pause_etape)

                    tmp_workout.addEtape(tmp_etape)
                }
            }
            workouts.add(tmp_workout)

        }
        return workouts
    }

    fun writeToSDCardWorkoutXML(fileName: String, arrayworkout: ArrayList<Workout>) {
        var fileName = fileName
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var writer: FileWriter? = null
            var workoutTmp: Workout
            var etapeTmp: Etape
            try {
                val file = File(internal, shortlocalpath)

                writer = FileWriter(file)
                writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
                writer.write("<workouts>\n")
                for (i in arrayworkout.indices) {
                    workoutTmp = arrayworkout[i]
                    writer.write(
                        "<workout name_workout=\"" + workoutTmp.toString()
                                + "\" nb_etape=\"" + workoutTmp.nbEtape + "\">\n"
                    )
                    println(workoutTmp.toString())
                    for (j in 0 until workoutTmp.nbEtape) {
                        etapeTmp = workoutTmp.list_etape.get(j)
                        writer.write(
                            "<etape nom_eta=\"" + etapeTmp.titre + "\" desc_eta=\"" + etapeTmp.desc
                                    + "\" temps=\"" + etapeTmp.temps + "\" pause=\"" + etapeTmp.pause + "\"></etape>\n"
                        )
                    }
                    writer.write("</workout>\n")
                }
                writer.write("</workouts>\n")
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (writer != null) {
                    try {
                        writer.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }

    fun deleteWorkoutByNumber(indice: Int, v: View): ArrayList<Workout>? {
        try {
            val file = File(internal + shortlocalpath)
            val streamFile = FileInputStream(file)
            val doc = getDocument(streamFile)
            val nodeList = doc!!.getElementsByTagName("workout")
            val finalWorkouts = getXMLWorkoutValue(nodeList)
            Collections.sort(
                finalWorkouts
            ) { o1, o2 -> o1.toString().compareTo(o2.toString()) }
            finalWorkouts.removeAt(indice)
            writeToSDCardWorkoutXML("", finalWorkouts)
            return finalWorkouts


        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}
