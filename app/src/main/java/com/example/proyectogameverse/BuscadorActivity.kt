package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class BuscadorActivity : AppCompatActivity() {
    private var id_perfil : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        configAPI()

        val bbuscar : Button = findViewById(R.id.bBuscar)

        bbuscar.setOnClickListener{
            buscarRegistro()
        }
    }

    private fun configAPI() {
        val intent = intent
        if(intent != null){
            id_perfil = intent.getIntExtra("id_perfil",0)
        }
    }

    private fun buscarRegistro() { //Script para cargar los datos seleccionados
        val etbuscar : EditText = findViewById(R.id.etBuscar)
        val buscar : String = etbuscar.text.toString()

        val rbpublicaciones : RadioButton = findViewById(R.id.rbPublicaciones)
        val rbgrupos : RadioButton = findViewById(R.id.rbGrupos)
        val rbperfiles : RadioButton = findViewById(R.id.rbPerfiles)

        val publicaciones = if (rbpublicaciones.isChecked) 1 else 0
        val grupos = if (rbgrupos.isChecked) 1 else 0
        val perfiles = if (rbperfiles.isChecked) 1 else 0

        val cbxboxbuscar : CheckBox = findViewById(R.id.cbXboxBuscar)
        val cbplaystationbuscar : CheckBox = findViewById(R.id.cbPlaystationBuscar)
        val cbnintendobuscar : CheckBox = findViewById(R.id.cbNintendoBuscar)

        val xbox = if (cbxboxbuscar.isChecked) 1 else 0
        val playstation = if (cbplaystationbuscar.isChecked) 1 else 0
        val nintendo = if (cbnintendobuscar.isChecked) 1 else 0

        val sgenerobuscar : Spinner = findViewById(R.id.sGeneroBuscar)
        val genero : String = sgenerobuscar.selectedItem as String

        val intent = Intent(this, ResultadoBuscarActivity::class.java)
        intent.putExtra("id_perfil",id_perfil)
        intent.putExtra("buscar",buscar)
        intent.putExtra("publicaciones",publicaciones)
        intent.putExtra("grupos",grupos)
        intent.putExtra("perfiles",perfiles)
        intent.putExtra("xbox",xbox)
        intent.putExtra("playstation",playstation)
        intent.putExtra("nintendo",nintendo)
        intent.putExtra("genero",genero)

        startActivity(intent)
    }
}