package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var url_buscar : String = ""

    private var id_perfil: Int = 0

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
            id_perfil = intent.getIntExtra("id_perfil",0)
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

            if(publicaciones != 0){ //Buscar publicaciones seleccionadas
                url_buscar = "http://3.22.175.225/gameverse_servidor/buscador/publicaciones.php"

                parametros["buscar"] = buscar
                parametros["xbox"] = xbox.toString()
                parametros["playstation"] = playstation.toString()
                parametros["nintendo"] = nintendo.toString()
                parametros["genero"] = genero

                val post : JSONObject = JSONObject(parametros)

                tvbuscar.setText("Publicaciones")

                buscarPublicaciones(post)
            }

            if(grupos != 0){ //Buscar grupos seleccionados
                url_buscar = "http://3.22.175.225/gameverse_servidor/buscador/grupos.php"

                parametros["id"] = id_perfil
                parametros["buscar"] = buscar
                parametros["xbox"] = xbox.toString()
                parametros["playstation"] = playstation.toString()
                parametros["nintendo"] = nintendo.toString()
                parametros["genero"] = genero

                val post : JSONObject = JSONObject(parametros)

                tvbuscar.setText("Grupos")

                buscarGrupos(post)
            }

            if(perfiles != 0){ //Buscar perfiles seleccionados
                url_buscar = "http://3.22.175.225/gameverse_servidor/buscador/usuarios.php"

                parametros["id"] = id_perfil
                parametros["buscar"] = buscar

                val post : JSONObject = JSONObject(parametros)

                tvbuscar.setText("Perfiles")

                buscarPerfiles(post)
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
            if(i != -1){
                val tvsinresultado : TextView = findViewById(R.id.tvSinResult)
                tvsinresultado.visibility = View.GONE
            }

            val busqueda = lista[i] as JSONObject

            var buscar = Buscar()

            buscar.id_publicacion = busqueda.getInt("id")
            buscar.titulo = busqueda.getString("titulo")
            buscar.descripcion = busqueda.getString("descripcion")
            buscar.creacion = busqueda.getString("creacion")
            buscar.thumbnail = busqueda.getString("url")
            buscar.nombre = busqueda.getString("usuario")

            buscar.id_usuario = id_perfil

            adapter.guardar(buscar)
        }
    }

    private fun buscarGrupos(post: JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_buscar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    llenarGrupos(response.getJSONArray("lista"))
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

    private fun llenarGrupos(lista: JSONArray) {
        adapter.limpiar()

        for(i in 0 .. lista.length() - 1){
            if(i != -1){
                val tvsinresultado : TextView = findViewById(R.id.tvSinResult)
                tvsinresultado.visibility = View.GONE
            }

            val busqueda = lista[i] as JSONObject

            var buscar = Buscar()

            buscar.id_grupos = busqueda.getInt("id")
            buscar.nombre_grupo = busqueda.getString("nombre")
            buscar.descripcion_grupo = busqueda.getString("descripcion")
            buscar.thumbnail = busqueda.getString("icono")

            buscar.id_usuario = id_perfil

            adapter.guardar(buscar)
        }
    }

    private fun buscarPerfiles(post: JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_buscar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    llenarPerfiles(response.getJSONArray("lista"))
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

    private fun llenarPerfiles(lista: JSONArray) {
        adapter.limpiar()

        for(i in 0 .. lista.length() - 1){
            if(i != -1){
                val tvsinresultado : TextView = findViewById(R.id.tvSinResult)
                tvsinresultado.visibility = View.GONE
            }

            val busqueda = lista[i] as JSONObject

            var buscar = Buscar()

            buscar.id_perfil= busqueda.getInt("id")
            buscar.nombre = busqueda.getString("nombre")
            buscar.email = busqueda.getString("email")
            buscar.descripcion_perfil = busqueda.getString("descripcion")
            buscar.videojuego = busqueda.getString("videojuego")
            buscar.thumbnail = busqueda.getString("url")

            buscar.id_usuario = id_perfil

            adapter.guardar(buscar)
        }
    }
}