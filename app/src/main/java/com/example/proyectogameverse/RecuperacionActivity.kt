package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RecuperacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion)

        val brecuperar : Button = findViewById(R.id.bRecuperar)

        brecuperar.setOnClickListener{
            verificarPerfil()
        }
    }

    private fun verificarPerfil() {
        val etperfil : EditText = findViewById(R.id.etPerfil2)
        val perfil : String = etperfil.text.toString()

        if(perfil.isNotEmpty()){
            val intent = Intent(this,RecuperacionActivity2::class.java)

            startActivity(intent)
        }else{
            Toast.makeText(this,"Introduce el nombre o correo electrónico",Toast.LENGTH_SHORT).show()
        }
    }
}