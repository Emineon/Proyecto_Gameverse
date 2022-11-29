package com.example.proyectogameverse

import android.app.VoiceInteractor
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class DatosActivity : AppCompatActivity() {
    private var url_actualizar = "http://192.168.1.87/gameverse_preservidor/usuario/actualizar.php"

    private lateinit var etnombre : EditText
    private lateinit var etdescripcion : EditText
    private lateinit var etfecha : EditText
    private lateinit var etvideojuego : EditText

    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var fecha : String = ""
    private var descripcion : String = ""
    private var videojuego : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)

        ConfigIU()

        val bconfirmar : Button = findViewById(R.id.bConfirmar2)

        bconfirmar.setOnClickListener{
            mensajeConfirmacion()
        }
    }

    private fun ConfigIU() {
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")){
            id_perfil = intent.getIntExtra("id_perfil",0)
            nombre = intent.getStringExtra("nombre").toString()
            descripcion = intent.getStringExtra("descripcion").toString()
            fecha = intent.getStringExtra("fecha").toString()
            videojuego = intent.getStringExtra("videojuego").toString()

            etnombre = findViewById(R.id.etNombre2)
            etdescripcion = findViewById(R.id.etDescripcion)
            etfecha = findViewById(R.id.etCumple)
            etvideojuego = findViewById(R.id.etVideojuego)

            etnombre.setText(nombre)
            etdescripcion.setText(descripcion)
            etfecha.setText(fecha)
            etvideojuego.setText(videojuego)
        }
    }

    private fun mensajeConfirmacion() {
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                       confirmarActualizar()
                    })
                setNegativeButton(R.string.cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }

            builder.setTitle("¿Quieres guardar los cambios?")

            builder.create()
            builder.show()
        }
    }

    private fun confirmarActualizar() {
        etnombre = findViewById(R.id.etNombre2)
        val nombre : String = etnombre.text.toString()

        etdescripcion = findViewById(R.id.etDescripcion)
        val descripcion : String = etdescripcion.text.toString()

        val etcumple : EditText = findViewById(R.id.etCumple)
        val cumple : String = etcumple.text.toString()

        etvideojuego = findViewById(R.id.etVideojuego)
        val videojuego : String = etvideojuego.text.toString()

        val parametros = mutableMapOf<String, Any?>()

        if(nombre.isNotEmpty()){
            parametros["id"] = id_perfil
            parametros["nombre"] = nombre
            parametros["descripcion"] = descripcion
            parametros["nacimiento"] = cumple
            parametros["videojuego"] = videojuego

            val post : JSONObject = JSONObject(parametros)

            actualizarDatos(post)
        }else{
            Toast.makeText(this,"Vuelve a introducir un nombre de perfil",Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarDatos(post: JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_actualizar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    finish()
                }
            },
            {
                errorResponse ->
                Toast.makeText(applicationContext, "Error en el acceso de sistema", Toast.LENGTH_SHORT).show()
            }
        )

        quece.add(request)
    }
}