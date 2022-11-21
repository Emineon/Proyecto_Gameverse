package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class PublicacionesActivity : AppCompatActivity() {
    private var url_listar : String = "http://192.168.1.87/gameverse_preservidor/publicaciones/listar.php"
    private var id_perfil : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicaciones)

        val bcrear : Button = findViewById(R.id.bCrear)

        bcrear.setOnClickListener{
            val intent = Intent(this, CrearActivity::class.java)
            intent.putExtra("id_perfil",id_perfil)

            startActivity(intent)
        }
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
        if(intent != null){
            id_perfil = intent.getIntExtra("id_perfil",-1)
            url_listar += "?id_perfil = $id_perfil"
        }
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
        for(i in 0 .. lista.length() - 1){
            val publicaciones = lista[i] as JSONObject
        }
    }
}