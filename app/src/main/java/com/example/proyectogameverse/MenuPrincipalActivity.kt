package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import org.json.JSONArray
import org.json.JSONObject

class MenuPrincipalActivity : AppCompatActivity() {
    private var url_perfil : String = "http://192.168.1.87/gameverse_preservidor/perfil.php"
    private lateinit var nombre : String
    private var id_perfil : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        val chippublicaciones : Chip = findViewById(R.id.chipPublicaciones)

        chippublicaciones.setOnClickListener{
            val intent = Intent(this, PublicacionesActivity::class.java)
            intent.putExtra("id_perfil",id_perfil)
            Log.i("",id_perfil.toString())

            startActivity(intent)
        }

        val chipgrupos : Chip = findViewById(R.id.chipGrupos)

        chipgrupos.setOnClickListener{
            val intent = Intent(this, GruposActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        configAPI()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt("id",id_perfil)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        id_perfil = savedInstanceState.getInt("id")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuprincipal,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.opc_buscar -> {
                dirigirBusqueda()
            }
            R.id.opc_config -> {
                dirigirAjustes()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun configAPI() {
        val intent = intent
        if(intent != null && intent.hasExtra("nombre")){
            nombre = intent.getStringExtra("nombre").toString()
            url_perfil += "?nombre=$nombre"

            leerPerfil()
        }
    }

    private fun leerPerfil() {
        val queue : RequestQueue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url_perfil,
            null,
            {
                    response ->
                if(response.getBoolean("exito")){
                    llenarIdentificador(response.getJSONArray("lista"))
                }
            },
            {
                    errorResponse ->
                Toast.makeText(this,"Error en el acceso a sistema", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun llenarIdentificador(lista: JSONArray) {
        for(i in 0 .. lista.length() - 1){
            val perfil = lista[i] as JSONObject

            id_perfil = perfil.getInt("id")
        }
    }

    private fun dirigirBusqueda() {
        val intent = Intent(this, BuscadorActivity::class.java)

        startActivity(intent)
    }

    private fun dirigirAjustes() {
        val intent = Intent(this, AjustesActivity::class.java)
        intent.putExtra("id_perfil",id_perfil)

        startActivity(intent)
    }
}