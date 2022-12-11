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
    private var url_actualizar = "http://3.22.175.225/gameverse_servidor/usuario/actualizar.php"

    private lateinit var etnombre : EditText
    private lateinit var etdescripcion : EditText
    private lateinit var etfecha : EditText
    private lateinit var etvideojuego : EditText

    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var nuevonombre : String = ""
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
        nuevonombre = etnombre.text.toString()

        etdescripcion = findViewById(R.id.etDescripcion)
        val descripcion : String = etdescripcion.text.toString()

        val etcumple : EditText = findViewById(R.id.etCumple)
        val cumple : String = etcumple.text.toString()

        etvideojuego = findViewById(R.id.etVideojuego)
        val videojuego : String = etvideojuego.text.toString()

        val parametros = mutableMapOf<String, Any?>()

        if(nuevonombre.isNotEmpty()){
            parametros["id"] = id_perfil
            parametros["accion"] = "Datos"
            parametros["nombre"] = nuevonombre
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
                    val intent = Intent(this,AjustesActivity::class.java)
                    intent.putExtra("id_perfil",id_perfil)
                    intent.putExtra("nombre",nuevonombre)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
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