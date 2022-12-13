package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class VerGrupoActivity : AppCompatActivity() {
    private var url_comentarios : String = "http://3.22.175.225/gameverse_servidor/grupos/comentarios/listar.php"
    private val url_crear_comentario : String = "http://3.22.175.225/gameverse_servidor/grupos/comentarios/crear.php"

    private var id_grupo : Int = 0
    private var nombre : String = ""
    private var descripcion : String = ""
    private var icono : String = ""

    private var id_perfil: Int = 0

    private lateinit var adapter : ComentariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_grupo)

        ConfigIU()

        val rvcomentarios : RecyclerView = findViewById(R.id.rvComentarGrupo)
        adapter = ComentariosAdapter()

        rvcomentarios.adapter = adapter
        rvcomentarios.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        val bpublicar : Button = findViewById(R.id.bPublicar2)

        bpublicar.setOnClickListener{
            publicarComentario()
        }
    }

    private fun ConfigIU() {
        val intent = intent
        if(intent != null && intent.hasExtra("id")) {
            id_grupo = intent.getIntExtra("id", 0)
            nombre = intent.getStringExtra("nombre").toString()
            descripcion = intent.getStringExtra("descripcion").toString()
            icono = intent.getStringExtra("icono").toString()
            id_perfil = intent.getIntExtra("id_usuario", 0)

            leerComentarios()
        }
    }

    private fun leerComentarios() {
        val parametros = mutableMapOf<String, Any?>()

        parametros["id_grupo"] = id_grupo

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

    private fun publicarComentario() {
        val etcomentario : EditText = findViewById(R.id.etComentarGrupo)
        val comentario : String = etcomentario.text.toString()

        if(comentario.isNotEmpty()){
            val parametros = mutableMapOf<String, Any?>()

            parametros["id_grupo"] = id_grupo
            parametros["id_perfil"] = id_perfil
            parametros["comentario"] = comentario

            val post : JSONObject = JSONObject(parametros)

            enviarComentario(post)
        }
    }

    private fun enviarComentario(post: JSONObject) {
        val queue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_crear_comentario,
            post,
            {
                    response ->
                if(response.getBoolean("exito")){
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(getIntent())
                    overridePendingTransition(0, 0)
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
                }
            },
            {
                    errorResponse ->
                Toast.makeText(applicationContext, "Error en el acceso de BD", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
}