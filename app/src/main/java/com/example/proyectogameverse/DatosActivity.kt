package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class DatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)

        val bconfirmar : Button = findViewById(R.id.bConfirmar2)

        bconfirmar.setOnClickListener{
            confirmarActualizar()
        }
    }

    private fun confirmarActualizar() {
        val etnombre : EditText = findViewById(R.id.etNombre2)
        val nombre : String = etnombre.text.toString()

        val etdescripcion : EditText = findViewById(R.id.etDescripcion)
        val descripcion : String = etdescripcion.text.toString()

        val etcumple : EditText = findViewById(R.id.etCumple)
        val cumple : String = etcumple.text.toString()

        val etvideojuego : EditText = findViewById(R.id.etVideojuego)
        val videojuego : String = etvideojuego.text.toString()
    }
}