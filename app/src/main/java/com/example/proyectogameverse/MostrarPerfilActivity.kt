package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MostrarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_perfil)
    }

    override fun onStart() {
        super.onStart()

        configIU()
    }

    private fun configIU() {
        if(intent != null){
            val nombre = intent.getStringExtra("nombre").toString()
            val descripcion = intent.getStringExtra("descripcion").toString()
            val email = intent.getStringExtra("email").toString()
            val videojuego = intent.getStringExtra("videojuego").toString()
            val imagen = intent.getStringExtra("imagen").toString()

            val tvnombre : TextView = findViewById(R.id.tvNombreAjeno)
            val tvdescripcion : TextView = findViewById(R.id.tvDescAjeno)
            val etemail : TextView = findViewById(R.id.etCorreoAjeno)
            val etvideojuego : TextView = findViewById(R.id.etVideojuegoAjeno)
            val ivperfil : ImageView = findViewById(R.id.ivPerfilAjeno)

            tvnombre.setText(nombre)
            tvdescripcion.setText(descripcion)
            etemail.setText(email)
            etvideojuego.setText(videojuego)

            if(imagen != ""){
                ivperfil.setBackgroundDrawable(null)
                Picasso.get().load(imagen).into(ivperfil)
            }
        }
    }
}