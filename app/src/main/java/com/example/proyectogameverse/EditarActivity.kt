package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class EditarActivity : AppCompatActivity() {
    private var url_borrar : String = ""

    private var id : Int = 0
    private var titulo : String = ""
    private var descripcion : String = ""
    private var xbox : Boolean = false
    private var playstation : Boolean = false
    private var nintendo : Boolean = false
    private var genero : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)

        ConfigIU()
    }

    private fun ConfigIU() {
        val intent = intent
        if(intent != null && intent.hasExtra("id")){
            id = intent.getIntExtra("id",0)
            titulo = intent.getStringExtra("titulo").toString()
            descripcion = intent.getStringExtra("descripcion").toString()
            xbox = intent.getBooleanExtra("xbox",false)
            playstation = intent.getBooleanExtra("playstation",false)
            nintendo = intent.getBooleanExtra("nintendo",false)
            genero = intent.getStringExtra("genero").toString()

            val tvpublicacion : TextView = findViewById(R.id.tvPublicacion)
            val tvdescripcion : TextView = findViewById(R.id.tvDescripcion)

            tvpublicacion.text = titulo
            tvdescripcion.text = descripcion
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menueditar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.opc_editar){
            intentEditar()
        }
        if(item.itemId == R.id.opc_eliminar){
            url_borrar = "http://192.168.1.87/gameverse_preservidor/publicaciones/modificar.php"
            url_borrar += "?id_publicaciones=$id"
            mensajeBorrar()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun intentEditar() {
        val intent = Intent(this, CrearActivity::class.java)

        intent.putExtra("id_publicacion",id)
        intent.putExtra("titulo",titulo)
        intent.putExtra("descripcion",descripcion)
        intent.putExtra("xbox",xbox)
        intent.putExtra("playstation",playstation)
        intent.putExtra("nintendo",nintendo)
        intent.putExtra("genero",genero)

        startActivity(intent)
    }

    private fun mensajeBorrar() {
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        borrarPublicacion()
                    })
                setNegativeButton(R.string.cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                    })
            }

            builder.setTitle(R.string.confirmacion)

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }

    private fun borrarPublicacion() {
        val queue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE,
            url_borrar,
            null,
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
                Toast.makeText(applicationContext, "Error en el acceso a sistema", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
}