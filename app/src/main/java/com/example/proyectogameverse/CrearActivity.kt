package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CrearActivity : AppCompatActivity() {
    private var url_crear : String = "http://192.168.1.87/gameverse_preservidor/publicaciones/crear.php"
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        configAPI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menucrear,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.opc_crear){
            crearPublicacion()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun configAPI() {
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")){
            id = intent.getIntExtra("id_perfil",0)
        }
    }

    private fun crearPublicacion() {
        val ettitulo : EditText = findViewById(R.id.etTitulo)
        val titulo : String = ettitulo.text.toString()

        val etDesc : EditText = findViewById(R.id.etDescPublicacion)
        val descripcion : String = etDesc.text.toString()

        val cbxbox : CheckBox = findViewById(R.id.cbXbox)
        val cbplaystation : CheckBox = findViewById(R.id.cbPlaystation)
        val cbnintendo : CheckBox = findViewById(R.id.cbNintendo)

        val xbox : Int = if (cbxbox.isChecked) 1 else 0
        val playstation : Int = if (cbplaystation.isChecked) 1 else 0
        val nintendo : Int = if (cbnintendo.isChecked) 1 else 0

        val sgenero : Spinner = findViewById(R.id.sGenero)
        val genero : String = sgenero.selectedItem as String

        val parametros = mutableMapOf<String, Any?>()

        parametros["id"] = id
        parametros["titulo"] = titulo
        parametros["descripcion"] = descripcion
        parametros["xbox"] = xbox.toString()
        parametros["playstation"] = playstation.toString()
        parametros["nintendo"] = nintendo.toString()
        parametros["genero"] = genero

        val post : JSONObject = JSONObject(parametros)

        enviarPublicacion(post)
    }

    private fun enviarPublicacion(post: JSONObject) {
        val queue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_crear,
            post,
            {
                    response ->
                if(response.getBoolean("exito")){
                    finish()
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