package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class PublicacionesActivity : AppCompatActivity() {
    private var url_listar : String = ""
    private var id_perfil : Int = 0
    private lateinit var adapter : PublicacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicaciones)

        val bcrear : Button = findViewById(R.id.bCrear)

        bcrear.setOnClickListener{
            val intent = Intent(this, CrearActivity::class.java)
            intent.putExtra("id_perfil",id_perfil)

            startActivity(intent)
        }

        val rvpublicaciones : RecyclerView = findViewById(R.id.rvMenu)
        adapter = PublicacionAdapter()

        rvpublicaciones.adapter = adapter
        rvpublicaciones.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
    }

    override fun onStart() {
        super.onStart()

        ConfigAPI()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt("id",id_perfil)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        id_perfil = savedInstanceState.getInt("id")
    }

    private fun ConfigAPI() {
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")){
            id_perfil = intent.getIntExtra("id_perfil",-1)
        }
        url_listar = "http://3.22.175.225/gameverse_servidor/publicaciones/listar.php?id_perfil=$id_perfil"
        leerLista()
    }

    private fun leerLista() {
        val queue : RequestQueue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url_listar,
            null,
            {
                    response ->
                if(response.getBoolean("exito")){
                    llenarLista(response.getJSONArray("lista"))
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()
                }
            },
            {
                    errorResponse ->
                Toast.makeText(this,"Error en el acceso a sistema", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun llenarLista(lista: JSONArray) {
        adapter.limpiar()

        for(i in 0 .. lista.length() - 1){
            if(i != -1){
                val tvcontenido : TextView = findViewById(R.id.tvContenido)
                tvcontenido.visibility = View.GONE
            }

            val publicaciones = lista[i] as JSONObject

            var publicacion = Publicacion()

            publicacion.id_publicacion = publicaciones.getInt("id")
            publicacion.titulo = publicaciones.getString("titulo")
            publicacion.thumbnail = publicaciones.getString("imagen")

            adapter.guardar(publicacion)
        }
    }
}