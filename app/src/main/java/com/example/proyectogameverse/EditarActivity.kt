package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class EditarActivity : AppCompatActivity() {
    private var url_borrar : String = ""
    private var url_listar : String = ""

    private var id : Int = 0
    private var titulo : String = ""
    private var descripcion : String = ""
    private var xbox : Boolean = false
    private var playstation : Boolean = false
    private var nintendo : Boolean = false
    private var genero : String = ""
    private var nombre_archivo : String = ""
    private var url_imagen = ""
    private var actualizacion : String = ""

    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)

        storage = Firebase.storage

        ConfigIU()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt("id_publicacion",id)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        id = savedInstanceState.getInt("id_publicacion")
    }

    private fun ConfigIU() {
        val intent = intent
        if(intent != null && intent.hasExtra("id")){
            id = intent.getIntExtra("id",0)

            url_listar = "http://3.22.175.225/gameverse_servidor/publicaciones/listar.php?id_publicaciones=$id"
            leerLista()
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
            url_borrar = "http://3.22.175.225/gameverse_servidor/publicaciones/modificar.php?id_publicaciones=$id"
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
        intent.putExtra("nombre_archivo",nombre_archivo)
        intent.putExtra("imagen",url_imagen)

        startActivity(intent)
    }

    private fun mensajeBorrar() {
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        borrarImagen()
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

    private fun borrarImagen() {
        if(url_imagen != "") {
            var storageRef = storage.reference
            var eliminar = storageRef.child("imagenes/${nombre_archivo}")

            eliminar.delete().addOnSuccessListener {
                borrarPublicacion()
            }.addOnFailureListener() {
                Toast.makeText(
                    applicationContext,
                    "Ocurrio un error para eliminar el archivo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else{
            borrarPublicacion()
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

            id = publicaciones.getInt("id")
            titulo = publicaciones.getString("titulo")
            descripcion = publicaciones.getString("descripcion")
            xbox = publicaciones.getBoolean("xbox")
            playstation = publicaciones.getBoolean("playstation")
            nintendo = publicaciones.getBoolean("nintendo")
            genero = publicaciones.getString("genero")
            nombre_archivo = publicaciones.getString("nombre_imagen")
            url_imagen = publicaciones.getString("imagen")
            actualizacion = publicaciones.getString("actualizacion")
        }

        val tvpublicacion : TextView = findViewById(R.id.tvPublicacion)
        val tvdescripcion : TextView = findViewById(R.id.tvDescripcion)
        val ivimagen : ImageView = findViewById(R.id.ivImagen)
        val tvfecha : TextView = findViewById(R.id.tvFecha)

        tvpublicacion.text = titulo
        tvdescripcion.text = descripcion
        tvfecha.text = "Fecha de ??ltima modificaci??n: $actualizacion"

        if(url_imagen != ""){
            Picasso.get().load(url_imagen).into(ivimagen)
        }else{
            ivimagen.setImageResource(R.drawable.inc_videojuego)
        }
    }
}