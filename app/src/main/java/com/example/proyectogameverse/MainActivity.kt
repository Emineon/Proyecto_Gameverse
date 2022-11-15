package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val biniciar : Button = findViewById(R.id.bIniciar)

        biniciar.setOnClickListener{
            verificarPerfil()
        }

        val bolvidar : Button = findViewById(R.id.bOlvidar)

        bolvidar.setOnClickListener{
            val intent = Intent(this, RecuperacionActivity::class.java)

            startActivityForResult(intent,2)
        }

        val bregistrar : Button = findViewById(R.id.bRegistrar)

        bregistrar.setOnClickListener{
            val intent = Intent(this, CrearPerfilActivity::class.java)

            startActivityForResult(intent,3)
        }
    }

    private fun verificarPerfil() {
        val etperfil : EditText = findViewById(R.id.etPerfil)
        val perfil : String = etperfil.text.toString()

        val etpassword : EditText = findViewById(R.id.etPassword)
        val password : String = etpassword.text.toString()

        if(perfil.isNotEmpty()){
            if(password.isNotEmpty()){
                val intent = Intent(this, MenuPrincipalActivity::class.java)

                startActivityForResult(intent,1)
            }else{
                Toast.makeText(this,"No se ingreso ningún dato",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"No se ingreso ningún dato",Toast.LENGTH_SHORT).show()
        }
    }
}