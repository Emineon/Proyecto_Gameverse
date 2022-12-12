package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
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

class PerfilActivity : AppCompatActivity() {
    private var url_publicaciones : String = ""

    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var email : String = ""
    private var descripcion : String = ""
    private var videojuego : String = ""
    private var imagen : String = ""

    private var id_usuario : Int = 0

    private lateinit var adapter : BuscadorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val rvpublicaciones : RecyclerView = findViewById(R.id.rvPublicaciones)
        adapter = BuscadorAdapter()

        rvpublicaciones.adapter = adapter
        rvpublicaciones.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        val ibperfil : ImageButton = findViewById(R.id.ibPerfilAjeno)
        ibperfil.setOnClickListener{
            mostrarPerfil()
        }
    }

    override fun onStart() {
        super.onStart()

        ConfigAPI()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt("usuario",id_usuario)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        id_usuario = savedInstanceState.getInt("usuario")
    }

    private fun ConfigAPI() {
        val intent = intent
        if(intent != null && intent.hasExtra("id")){
            id_perfil = intent.getIntExtra("id", 0)
            nombre = intent.getStringExtra("nombre").toString()
            email = intent.getStringExtra("email").toString()
            descripcion = intent.getStringExtra("descripcion").toString()
            videojuego = intent.getStringExtra("videojuego").toString()
            imagen = intent.getStringExtra("imagen").toString()

            id_usuario = intent.getIntExtra("id_usuario",0)

            val ibperfil : ImageView = findViewById(R.id.ibPerfilAjeno)
            val tvnombre : TextView = findViewById(R.id.tvPerfilAjeno)

            tvnombre.setText(nombre)

            if(imagen != "") {
                Picasso.get().load(imagen).into(ibperfil)
            }

            leerPublicaciones()
        }
    }

    private fun leerPublicaciones() {
        url_publicaciones = "http://3.22.175.225/gameverse_servidor/menu/publicaciones.php?id_perfil=$id_perfil"

        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url_publicaciones,
            null,
            {
                    response ->
                if(response.getBoolean("exito")){
                    llenarPerfiles(response.getJSONArray("lista"))
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje, Toast.LENGTH_SHORT).show()
                }
            },
            {
                    errorResponse ->
                Toast.makeText(applicationContext,"Error en el acceso a sistema", Toast.LENGTH_SHORT).show()
            }
        )

        quece.add(request)
    }

    private fun llenarPerfiles(lista: JSONArray) {
        adapter.limpiar()

        for(i in 0 .. lista.length() - 1){
            val busqueda = lista[i] as JSONObject

            var buscar = Buscar()

            buscar.id_publicacion = busqueda.getInt("id")
            buscar.titulo = busqueda.getString("titulo")
            buscar.descripcion = busqueda.getString("descripcion")
            buscar.creacion = busqueda.getString("creacion")
            buscar.thumbnail = busqueda.getString("url")
            buscar.nombre = busqueda.getString("usuario")

            buscar.id_usuario = id_usuario

            adapter.guardar(buscar)
        }
    }

    private fun mostrarPerfil() {
        val intent = Intent(this, MostrarPerfilActivity::class.java)
        intent.putExtra("nombre",nombre)
        intent.putExtra("descripcion",descripcion)
        intent.putExtra("email",email)
        intent.putExtra("videojuego",videojuego)
        intent.putExtra("imagen",imagen)

        startActivity(intent)
    }
}