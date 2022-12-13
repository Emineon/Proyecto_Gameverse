package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class CrearGrupoActivity : AppCompatActivity() {
    private var url_crear : String = "http://3.22.175.225/gameverse_servidor/grupos/crear.php"

    private var id_perfil : Int = 0

    private var url_imagen : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_grupo)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menucrear, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.opc_crear) {
            lanzarGrupo()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        ConfigUI()
    }

    private fun ConfigUI() {
        if(intent != null && intent.hasExtra("id_perfil")) {
            id_perfil = intent.getIntExtra("id_perfil", 0)
        }
    }

    private fun lanzarGrupo() {
        val ettitulo : EditText = findViewById(R.id.etNombreGroup)
        val titulo : String = ettitulo.text.toString()

        val etDesc : EditText = findViewById(R.id.etDescGroup)
        val descripcion : String = etDesc.text.toString()

        val cbxbox : CheckBox = findViewById(R.id.cbXboxGrupo)
        val cbplaystation : CheckBox = findViewById(R.id.cbPlaystationGrupo)
        val cbnintendo : CheckBox = findViewById(R.id.cbNintendoGrupo)

        val xbox : Int = if (cbxbox.isChecked) 1 else 0
        val playstation : Int = if (cbplaystation.isChecked) 1 else 0
        val nintendo : Int = if (cbnintendo.isChecked) 1 else 0

        val sgenero : Spinner = findViewById(R.id.sGeneroGrupo)
        val genero : String = sgenero.selectedItem as String

        if(titulo.isNotEmpty() && descripcion.isNotEmpty() && genero.isNotEmpty()){
            if(xbox.equals(1) || playstation.equals(1) || nintendo.equals(1)) {
                val parametros = mutableMapOf<String, Any?>()

                parametros["id"] = id_perfil
                parametros["nombre_grupo"] = titulo
                parametros["descripcion"] = descripcion
                parametros["xbox"] = xbox.toString()
                parametros["playstation"] = playstation.toString()
                parametros["nintendo"] = nintendo.toString()
                parametros["genero"] = genero
                parametros["icono"] = url_imagen

                val post: JSONObject = JSONObject(parametros)

                enviarGrupo(post)
            }else{
                Toast.makeText(this,"Escoge a menos una plataforma", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Llenar el formulario para publicar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarGrupo(post: JSONObject) {
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