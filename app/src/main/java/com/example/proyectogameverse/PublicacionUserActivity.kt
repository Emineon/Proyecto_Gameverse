package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class PublicacionUserActivity : AppCompatActivity() {
    private var url_comentarios : String = "http://3.22.175.225/gameverse_servidor/buscador/comentarios.php"

    private var id_publicacion : Int = 0
    private var titulo : String = ""
    private var descripcion : String = ""
    private var creacion : String = ""
    private var imagen : String = ""
    private var id_perfil : Int = 0
    private var usuario : String = ""

    private lateinit var adapter : ComentariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicacion_user)

        val rvcomentarios : RecyclerView = findViewById(R.id.rvComentarios)
        adapter = ComentariosAdapter()

        rvcomentarios.adapter = adapter
        rvcomentarios.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
    }

    override fun onStart() {
        super.onStart()

        ConfigIU()
    }

    private fun ConfigIU() {
        val intent = intent
        if(intent != null && intent.hasExtra("id")){
            id_publicacion = intent.getIntExtra("id",0)
            titulo = intent.getStringExtra("titulo").toString()
            descripcion = intent.getStringExtra("descripcion").toString()
            creacion = "Fecha: "+intent.getStringExtra("creacion").toString()
            imagen = intent.getStringExtra("imagen").toString()
            id_perfil = intent.getIntExtra("id_perfil",0)
            usuario = intent.getStringExtra("usuario").toString()

            val tvtitulo : TextView = findViewById(R.id.tvTituloUser)
            val tvfecha : TextView = findViewById(R.id.tvFechaCreacion)
            val tvdescripcion : TextView = findViewById(R.id.tvDesc)
            val ivpublicacion : ImageView = findViewById(R.id.ivPublicacion)

            tvtitulo.setText(titulo)
            tvfecha.setText(creacion)
            tvdescripcion.setText(descripcion)

            if(imagen != "") {
                Picasso.get().load(imagen).into(ivpublicacion)
            }

            leerComentarios()
        }
    }

    private fun leerComentarios() {
        val parametros = mutableMapOf<String, Any?>()

        parametros["id_publicacion"] = id_publicacion

        val post : JSONObject = JSONObject(parametros)

        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_comentarios,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    llenarComentarios(response.getJSONArray("lista"))
                }
            },
            {
                errorResponse ->
                Toast.makeText(applicationContext,"Error en el acceso a sistema", Toast.LENGTH_SHORT).show()
            }
        )

        quece.add(request)
    }

    private fun llenarComentarios(lista: JSONArray) {
        adapter.limpiar()

        for(i in 0 .. lista.length() - 1){
            val comentarios = lista[i] as JSONObject

            var comentar = Comentarios()

            comentar.comentario = comentarios.getString("comentario")
            comentar.creacion = comentarios.getString("creacion")
            comentar.nombre_perfil = comentarios.getString("usuario")
            comentar.icono = comentarios.getString("icono")

            adapter.guardar(comentar)
        }
    }
}