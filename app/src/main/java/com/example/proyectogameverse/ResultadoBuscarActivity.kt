package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ResultadoBuscarActivity : AppCompatActivity() {
    private var url_buscar : String = "http://192.168.1.87/gameverse_preservidor/buscador/publicaciones.php"
    private lateinit var adapter : BuscadorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_buscar)

        val rvbusqueda : RecyclerView = findViewById(R.id.rvBusqueda)
        adapter = BuscadorAdapter()

        rvbusqueda.adapter = adapter
        rvbusqueda.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
    }

    override fun onStart() {
        super.onStart()

        ConfigUI()
    }

    private fun ConfigUI() {
        val intent = intent
        if(intent != null){
            val buscar = intent.getStringExtra("buscar")
            val publicaciones = intent.getIntExtra("publicaciones",0)
            val grupos = intent.getIntExtra("grupos",0)
            val perfiles = intent.getIntExtra("perfiles",0)
            val xbox = intent.getIntExtra("xbox",0)
            val playstation = intent.getIntExtra("playstation",0)
            val nintendo = intent.getIntExtra("nintendo",0)
            val genero = intent.getStringExtra("genero")

            val tvbuscar : TextView = findViewById(R.id.tvBuscar)

            val parametros = mutableMapOf<String, Any?>()

            parametros["buscar"] = buscar
            parametros["xbox"] = xbox.toString()
            parametros["playstation"] = playstation.toString()
            parametros["nintendo"] = nintendo.toString()
            parametros["genero"] = genero

            val post : JSONObject = JSONObject(parametros)

            if(publicaciones != 0){ //Buscar publicaciones seleccionadas
                tvbuscar.setText("Publicaciones")
                buscarPublicaciones(post)
            }

            if(grupos != 0){ //Buscar grupos seleccionados
                tvbuscar.setText("Grupos")
                Toast.makeText(applicationContext,"Se selecciono la opción de buscar 'grupos'", Toast.LENGTH_SHORT).show()
            }

            if(perfiles != 0){ //Buscar perfiles seleccionados
                tvbuscar.setText("Perfiles")
                Toast.makeText(applicationContext,"Se selecciono la opción de buscar 'perfiles'",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarPublicaciones(post: JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_buscar,
            post,
            {
                    response ->
                if(response.getBoolean("exito")){
                    llenarPublicaciones(response.getJSONArray("lista"))
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()
                }
            },
            {
                    errorResponse ->
                Toast.makeText(applicationContext,"Error en el acceso a sistema",Toast.LENGTH_SHORT).show()
            }
        )

        quece.add(request)
    }

    private fun llenarPublicaciones(lista: JSONArray) {
        adapter.limpiar()

        for(i in 0 .. lista.length() - 1){
            val busqueda = lista[i] as JSONObject

            var buscar = Buscar()

            buscar.id_publicacion = busqueda.getInt("id")
            buscar.titulo = busqueda.getString("titulo")
            buscar.descripcion = busqueda.getString("descripcion")
            buscar.usuario = busqueda.getString("usuario")

            adapter.guardar(buscar)
        }
    }
}