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

class AjustesActivity : AppCompatActivity() {
    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var fecha : String = ""
    private var descripcion : String = ""
    private var videojuego : String = ""

    private lateinit var urlguardado : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        ConfigUI()

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

            val tvnombre : TextView = findViewById(R.id.tvNombre)
            tvnombre.setText(nombre)
        }
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
                val ibperfil : ImageButton = findViewById(R.id.ibPerfil)

                val fullPhotoUri = data.data
                Log.i("",fullPhotoUri.toString())
                val imageBitmap = BitmapFactory.decodeFile(fullPhotoUri.toString())//data.extras?.get("data") as Bitmap

                ibperfil.setImageBitmap(imageBitmap)
            }
        }
    }

    private fun cargaImagen(uri: Uri?) {
        if(uri != null){
            /*uri_guardado = uri
            nombre_imagen = generarNombreArchivo()

            mostrarImagen()

            Toast.makeText(this,"La imagen se esta cargando", Toast.LENGTH_SHORT).show()

            subirImagen()*/
        }
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

        startActivity(intent)
    }

    private fun cambiarCorreo() {
        val intent = Intent(this, CorreoActivity::class.java)

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