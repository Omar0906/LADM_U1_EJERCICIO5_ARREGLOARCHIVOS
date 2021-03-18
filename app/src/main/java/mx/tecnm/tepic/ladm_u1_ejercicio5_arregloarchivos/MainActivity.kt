package mx.tecnm.tepic.ladm_u1_ejercicio5_arregloarchivos

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils.split
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {
    var valor: String = ""
    var pos: Int = 0
    var arreglo: ArrayList<String> = arrayListOf("", "", "", "", "", "", "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var arregloMostrar: ArrayList<String>

        asignar.setOnClickListener {
            if(posicion.text.isNotBlank()) {
                valor = texto.text.toString()
                pos = posicion.text.toString().toInt()
                if (pos < 0 || pos > 9) {
                } else {
                    if (valor.isEmpty() == true) {
                        arreglo.set(pos, "")
                    } else {
                        arreglo.set(pos, valor)
                    }
                    texto.setText("")
                    posicion.setText("")
                }
            }
        }
        mostrar.setOnClickListener {
            arregloMostrar = ArrayList()
            var listaElementos = ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloMostrar)
            for ((indice, dato) in arreglo.withIndex()) {
                if (dato.trim() != "") {
                    arregloMostrar.add("[${indice}],${dato}")
                }
            }
            listaElementos.notifyDataSetChanged()
            lista.adapter = listaElementos
        }
        guardarinterna.setOnClickListener {
            var mensaje: String
            if (guardarEnInterna(arreglo.toString(), nombrearchivo.text.toString())) {
                mensaje = "Se ha guardado con éxito"
            } else {
                mensaje = "Error al guardar"
            }
        }
        leerinterna.setOnClickListener {
            var cadena = abrirDesdeInterna(nombrearchivo.text.toString())
            cadena = cadena.removeSurrounding("[","]")
            AlertDialog.Builder(this).setMessage(cadena).show()
            if (cadena != "") {
                arreglo = ArrayList<String>(cadena.split(","))
            }
        }
    }

    private fun guardarEnInterna(data: String, string: String): Boolean {
        try {
            var flujoSalida = OutputStreamWriter(openFileOutput(string, Context.MODE_PRIVATE))
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
        } catch (io: IOException) {
            AlertDialog.Builder(this).setMessage(io.message.toString()).show()
            return false
        }
        Toast.makeText(this,"Guardado exitosamente",Toast.LENGTH_LONG).show()
        return true
    }

    private fun abrirDesdeInterna(nombre: String): String {
        var data = ""
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nombre)))
            data = flujoEntrada.readLine()
            Toast.makeText(this, "Archivo cargado con éxito", Toast.LENGTH_LONG).show()
        } catch (io: IOException) {
            Toast.makeText(this, "Error: ${io.message.toString()}", Toast.LENGTH_LONG).show()
            return ""
        }
        return data
    }
}