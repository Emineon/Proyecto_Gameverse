package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class GruposActivity : AppCompatActivity() {
    private var url_listar : String = ""

    private var id_perfil: Int = 0

    private lateinit var adapter : BuscadorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupos)

        ConfigUI()

        val bgrupo : Button = findViewById(R.id.bGrupo)

        bgrupo.setOnClickListener{
            crearGrupo()
        }

        val rvgrupos : RecyclerView = findViewById(R.id.rvGrupos)
        adapter = BuscadorAdapter()

        rvgrupos.adapter = adapter
        rvgrupos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
    }

    private fun ConfigUI() {
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")) {
            id_perfil = intent.getIntExtra("id_perfil", 0)
        }

        url_listar = "http://3.22.175.225/gameverse_servidor/grupos/listar.php?id=$id_perfil"
        leerGrupos()
    }

    private fun leerGrupos() {
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
                    Toast.makeText(applicationContext,mensaje, Toast.LENGTH_SHORT).show()
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
            val grupos = lista[i] as JSONObject

            var grupo = Buscar()

            grupo.id_grupos = grupos.getInt("id_grupo")
            grupo.nombre_grupo = grupos.getString("nombre_grupo")
            grupo.descripcion_grupo = grupos.getString("descripcion")
            grupo.thumbnail = grupos.getString("icono")

            grupo.id_usuario = id_perfil

            adapter.guardar(grupo)
        }
    }

    private fun crearGrupo() {
        val intent = Intent(this, CrearGrupoActivity::class.java)
        intent.putExtra("id_perfil",id_perfil)
        startActivity(intent)
    }
}