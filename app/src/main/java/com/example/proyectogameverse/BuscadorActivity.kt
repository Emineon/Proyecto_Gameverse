package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class BuscadorActivity : AppCompatActivity() {
    private var url_buscar : String = "http://192.168.1.87/gameverse_preservidor/buscador/publicaciones.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        val bbuscar : Button = findViewById(R.id.bBuscar)

        bbuscar.setOnClickListener{
            buscarRegistro()
        }
    }

    private fun buscarRegistro() {
        val etbuscar : EditText = findViewById(R.id.etBuscar)
        val buscar : String = etbuscar.text.toString()

        val rbpublicaciones : RadioButton = findViewById(R.id.rbPublicaciones)
        val rbgrupos : RadioButton = findViewById(R.id.rbGrupos)
        val rbperfiles : RadioButton = findViewById(R.id.rbPerfiles)

        val cbxboxbuscar : CheckBox = findViewById(R.id.cbXboxBuscar)
        val cbplaystationbuscar : CheckBox = findViewById(R.id.cbPlaystationBuscar)
        val cbnintendobuscar : CheckBox = findViewById(R.id.cbNintendoBuscar)

        val xbox = if (cbxboxbuscar.isChecked) 1 else 0
        val playstation = if (cbplaystationbuscar.isChecked) 1 else 0
        val nintendo = if (cbnintendobuscar.isChecked) 1 else 0

        val sgenerobuscar : Spinner = findViewById(R.id.sGeneroBuscar)
        val genero : String = sgenerobuscar.selectedItem as String

        val parametros = mutableMapOf<String, Any?>()

        parametros["buscar"] = buscar
        parametros["xbox"] = xbox.toString()
        parametros["playstation"] = playstation.toString()
        parametros["nintendo"] = nintendo.toString()
        parametros["genero"] = genero

        val post : JSONObject = JSONObject(parametros)

        if(rbpublicaciones.isChecked){
            buscarPublicaciones(post)
        }

        if(rbgrupos.isChecked){
            Toast.makeText(applicationContext,"Se selecciono la opción de buscar 'grupos'",Toast.LENGTH_SHORT).show()
        }

        if(rbperfiles.isChecked){

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
                    //llenarLista(response.getJSONArray("lista"))
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ResultadoBuscarActivity::class.java)

                    startActivity(intent)
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

    /*private fun llenarLista(jsonArray: JSONArray) {

    }*/
}