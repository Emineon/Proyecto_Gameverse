package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class AjustesActivity : AppCompatActivity() {
    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var fecha : String = ""
    private var descripcion : String = ""
    private var videojuego : String = ""

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

            val tvnombre : TextView = findViewById(R.id.tvNombre)
            tvnombre.setText(nombre)
        }
    }

    private fun seleccionarFoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
            type = "image/*"
        }
        //if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent,1)
        //}
    }

    private fun cambiarDatos() {
        val intent = Intent(this, DatosActivity::class.java)

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