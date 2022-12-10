package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AjustesActivity : AppCompatActivity() {
    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var fecha : String = ""
    private var descripcion : String = ""
    private var videojuego : String = ""

    private var url_imagen : String = ""

    private var url_actualizar = "http://3.22.175.225/gameverse_servidor/usuario/actualizar.php"
    private var url_listar = "http://3.22.175.225/gameverse_servidor/menu/perfil.php"

    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        ConfigUI()

        storage = Firebase.storage

        val ibperfil : ImageButton = findViewById(R.id.ibPerfil)

        ibperfil.setOnClickListener{
            seleccionarFoto()
        }

        val bdatos : Button = findViewById(R.id.bDatos)

        bdatos.setOnClickListener{
            cambiarDatos()
        }

        val bcontraseña : Button = findViewById(R.id.bContraseña)

        bcontraseña.setOnClickListener{
            cambiarPassword()
        }

        val bcorreo : Button = findViewById(R.id.bCorreo)

        bcorreo.setOnClickListener{
            cambiarCorreo()
        }

        val bcerrar : Button = findViewById(R.id.bCerrar)

        bcerrar.setOnClickListener{
            mensajeConfirmacion()
        }
    }

    private fun ConfigUI() {
        val intent = intent
        if(intent != null){
            id_perfil = intent.getIntExtra("id_perfil",0)
            nombre = intent.getStringExtra("nombre").toString()
            fecha = intent.getStringExtra("fecha").toString()
            descripcion = intent.getStringExtra("descripcion").toString()
            videojuego = intent.getStringExtra("videojuego").toString()
            url_imagen = intent.getStringExtra("imagen").toString()

            val tvnombre : TextView = findViewById(R.id.tvNombre)
            tvnombre.setText(nombre)

            recargarPerfil()

            mostrarImagen()
        }
    }

    private fun recargarPerfil() {

    }

    private fun seleccionarFoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
            type = "image/*"
        }

        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data != null){
                val uri : Uri? = data.data

                cargaImagen(uri)
            }
        }
    }

    private fun cargaImagen(uri: Uri?) {
        if(uri != null){
            var uri_guardado = uri
            var nombre_imagen = generarNombreArchivo()

            Toast.makeText(this,"La imagen se esta cargando", Toast.LENGTH_SHORT).show()

            var storageRef = storage.getReference("perfiles/${nombre_imagen}")
            var carga = storageRef.putFile(uri_guardado)

            carga.addOnFailureListener{ error ->
                Toast.makeText(this,"Ocurrio un error mientras se cargaba la imagen",Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener{ task ->
                storageRef.downloadUrl.addOnSuccessListener{ it
                    url_imagen = it.toString()
                    mostrarImagen()
                    enviarImagen()
                }
            }
        }
    }

    private fun generarNombreArchivo(): Any {
        val time = Calendar.getInstance().time
        val formatime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
        val current = formatime.format(time)
        return "img_$current"
    }

    private fun mostrarImagen() {
        if(url_imagen != "") {
            val ibperfil: ImageButton = findViewById(R.id.ibPerfil)
            Picasso.get().load(url_imagen).into(ibperfil)
        }
    }

    private fun enviarImagen() {
        val parametros = mutableMapOf<String, Any?>()

        parametros["id"] = id_perfil
        parametros["accion"] = "Foto"
        parametros["imagen"] = url_imagen

        val post : JSONObject = JSONObject(parametros)

        val queue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_actualizar,
            post,
            {
                    response ->
                if(response.getBoolean("exito")){
                    Toast.makeText(applicationContext, "Se subio correctamente la imagen", Toast.LENGTH_SHORT).show()
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
                }
            },
            {
                    errorResponse ->
                Toast.makeText(applicationContext, "Error en el acceso de BD", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun cambiarDatos() {
        val intent = Intent(this, DatosActivity::class.java)
        intent.putExtra("id_perfil",id_perfil)
        intent.putExtra("nombre",nombre)
        intent.putExtra("descripcion",descripcion)
        intent.putExtra("fecha",fecha)
        intent.putExtra("videojuego",videojuego)

        startActivity(intent)
    }

    private fun cambiarPassword() {
        val intent = Intent(this, PasswordActivity::class.java)
        intent.putExtra("id_perfil",id_perfil)

        startActivity(intent)
    }

    private fun cambiarCorreo() {
        val intent = Intent(this, CorreoActivity::class.java)
        intent.putExtra("id_perfil",id_perfil)

        startActivity(intent)
    }

    private fun mensajeConfirmacion(){
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this@AjustesActivity, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        startActivity(intent)
                    })
                setNegativeButton(R.string.cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }

            builder.setTitle("¿Deseas cerrar sesión?")
            builder.setMessage("Se cerrará tu acceso a la plataforma, como; mensajes, imágenes, videos, entre otros")

            builder.create()
            builder.show()
        }
    }
}