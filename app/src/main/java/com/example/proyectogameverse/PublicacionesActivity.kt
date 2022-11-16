package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button

class PublicacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicaciones)

        val bcrear : Button = findViewById(R.id.bCrear)

        bcrear.setOnClickListener{
            val intent = Intent(this, CrearActivity::class.java)

            startActivity(intent)
        }
    }
}