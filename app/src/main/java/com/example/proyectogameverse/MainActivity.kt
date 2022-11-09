package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val biniciar : Button = findViewById(R.id.bIniciar)

        biniciar.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)

            startActivityForResult(intent,1)
        }

        val bregistrar : Button = findViewById(R.id.bRegistrar)

        bregistrar.setOnClickListener{
            val intent = Intent(this, CrearPerfilActivity::class.java)

            startActivityForResult(intent,2)
        }
    }
}