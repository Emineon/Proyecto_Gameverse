package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CrearActivity : AppCompatActivity() {
    private var url_crear : String = "http://3.22.175.225/gameverse_servidor/publicaciones/crear.php"
    private var url_modificar : String = "http://3.22.175.225/gameverse_servidor/publicaciones/modificar.php"
    private var url_imagen = ""

    private lateinit var ettitulo : EditText
    private lateinit var etDesc : EditText
    private lateinit var cbxbox : CheckBox
    private lateinit var cbplaystation : CheckBox
    private lateinit var cbnintendo : CheckBox
    private lateinit var sgenero : Spinner

    private var id : Int = 0
    private var id_publicacion : Int = 0
    private var nombre_imagen : String = ""
    private lateinit var uri_guardado : Uri
    
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        etDesc = findViewById(R.id.etDescPublicacion)

        configAPI()

        storage = Firebase.storage

        val bsubir : Button = findViewById(R.id.bSubir)

        bsubir.setOnClickListener{
            seleccionarImagen()
        }

        val cancelar_subida : ImageView = findViewById(R.id.cancelar_subida)

        cancelar_subida.setOnClickListener{
            borrarImagen()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent != null && intent.hasExtra("id_publicacion")){
            menuInflater.inflate(R.menu.menumodificar, menu)
        }else{
            menuInflater.inflate(R.menu.menucrear, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.opc_crear){
            crearPublicacion()
        }
        if(item.itemId == R.id.opc_cambios){
            editarPublicacion()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun configAPI() {
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")){
            id = intent.getIntExtra("id_perfil",0)
        }
        if(intent != null && intent.hasExtra("id_publicacion")){
            id_publicacion = intent.getIntExtra("id_publicacion",0)
            val titulo = intent.getStringExtra("titulo")
            val descripcion = intent.getStringExtra("descripcion")
            val xbox = intent.getBooleanExtra("xbox",false)
            val playstation = intent.getBooleanExtra("playstation",false)
            val nintendo = intent.getBooleanExtra("nintendo",false)
            val genero = intent.getStringExtra("genero").toString()
            nombre_imagen = intent.getStringExtra("nombre_archivo").toString()
            url_imagen = intent.getStringExtra("imagen").toString()

            ettitulo = findViewById(R.id.etTitulo)

            cbxbox = findViewById(R.id.cbXbox)
            cbplaystation = findViewById(R.id.cbPlaystation)
            cbnintendo = findViewById(R.id.cbNintendo)

            sgenero = findViewById(R.id.sGenero)

            ettitulo.setText(titulo)
            etDesc.setText(descripcion)

            cbxbox.isChecked = xbox
            cbplaystation.isChecked = playstation
            cbnintendo.isChecked = nintendo

            val arreglo = resources.getStringArray(R.array.genero)
            sgenero.setSelection(arreglo.indexOf(genero))

            mostrarImagen()
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
            type = "image/*"
        }

        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                if(data != null) {
                    val uri: Uri? = data.data

                    cargaImagen(uri)
                }
            }
        }
    }

    private fun cargaImagen(uri: Uri?) {
        if(uri != null){
            uri_guardado = uri
            nombre_imagen = generarNombreArchivo()

            mostrarImagen()

            Toast.makeText(this,"La imagen se esta cargando",Toast.LENGTH_SHORT).show()

            subirImagen()
        }
    }

    private fun subirImagen() {
        var storageRef = storage.getReference("imagenes/${nombre_imagen}")
        var carga = storageRef.putFile(uri_guardado)

        carga.addOnFailureListener{ error ->
            Toast.makeText(this,"Ocurrio un error mientras se cargaba la imagen",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener{ task ->
            storageRef.downloadUrl.addOnSuccessListener{ it
                url_imagen = it.toString()
                Toast.makeText(this,"Se cargo correctamente la imagen para subir",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generarNombreArchivo(): String {
        val time = Calendar.getInstance().time
        val formatime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
        val current = formatime.format(time)
        return "img_$current"
    }

    private fun mostrarImagen() {
        val tvimagen : TextView = findViewById(R.id.tvImagen)
        tvimagen.setText(nombre_imagen)

        val cancelar_subida : ImageView = findViewById(R.id.cancelar_subida)
        cancelar_subida.visibility = View.VISIBLE
    }

    private fun borrarImagen() {
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        var storageRef = storage.reference
                        var eliminar = storageRef.child("imagenes/${nombre_imagen}")

                        eliminar.delete().addOnSuccessListener{
                            url_imagen = ""
                            nombre_imagen = ""

                            val tvimagen : TextView = findViewById(R.id.tvImagen)
                            tvimagen.setText("")

                            val cancelar_subida : ImageView = findViewById(R.id.cancelar_subida)
                            cancelar_subida.visibility = View.GONE

                            if(id_publicacion != 0){
                                eliminacionArchivo()
                            }else{
                                Toast.makeText(this@CrearActivity,"La imagen quedo eliminada",Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener{
                            Toast.makeText(this@CrearActivity,"Ocurrio un error para dicha acción",Toast.LENGTH_SHORT).show()
                        }
                    })
                setNegativeButton(R.string.cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }

            builder.setTitle("¿Deseas eliminar la imagen?")
            builder.setMessage("Se quedaría por una imagen por default")

            builder.create()
            builder.show()
        }
    }

    private fun eliminacionArchivo() {
        val parametros = mutableMapOf<String, Any?>()

        parametros["id"] = id_publicacion
        parametros["nombre_imagen"] = nombre_imagen
        parametros["imagen"] = url_imagen

        val post : JSONObject = JSONObject(parametros)

        val url_borrar = "http://3.22.175.225/gameverse_preservidor/publicaciones/borrar_imagen.php"

        val queue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_borrar,
            post,
            {
                    response ->
                if(response.getBoolean("exito")){
                    Toast.makeText(applicationContext, "Se elimino correctamente la imagen", Toast.LENGTH_SHORT).show()
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

    private fun crearPublicacion() {
        ettitulo = findViewById(R.id.etTitulo)
        val titulo : String = ettitulo.text.toString()

        etDesc = findViewById(R.id.etDescPublicacion)
        val descripcion : String = etDesc.text.toString()

        cbxbox = findViewById(R.id.cbXbox)
        cbplaystation = findViewById(R.id.cbPlaystation)
        cbnintendo = findViewById(R.id.cbNintendo)

        val xbox : Int = if (cbxbox.isChecked) 1 else 0
        val playstation : Int = if (cbplaystation.isChecked) 1 else 0
        val nintendo : Int = if (cbnintendo.isChecked) 1 else 0

        sgenero = findViewById(R.id.sGenero)
        val genero : String = sgenero.selectedItem as String

        if(titulo.isNotEmpty() && descripcion.isNotEmpty() && genero.isNotEmpty()){
            val parametros = mutableMapOf<String, Any?>()

            parametros["id"] = id
            parametros["titulo"] = titulo
            parametros["descripcion"] = descripcion
            parametros["xbox"] = xbox.toString()
            parametros["playstation"] = playstation.toString()
            parametros["nintendo"] = nintendo.toString()
            parametros["genero"] = genero
            parametros["nombre_imagen"] = nombre_imagen
            parametros["imagen"] = url_imagen

            val post : JSONObject = JSONObject(parametros)

            enviarPublicacion(post)
        }else{
            Toast.makeText(this,"Llenar el formulario para publicar",Toast.LENGTH_SHORT).show()
        }
    }

    private fun editarPublicacion(){
        ettitulo = findViewById(R.id.etTitulo)
        val titulo : String = ettitulo.text.toString()

        val descripcion : String = etDesc.text.toString()

        cbxbox = findViewById(R.id.cbXbox)
        cbplaystation = findViewById(R.id.cbPlaystation)
        cbnintendo = findViewById(R.id.cbNintendo)

        val xbox : Int = if (cbxbox.isChecked) 1 else 0
        val playstation : Int = if (cbplaystation.isChecked) 1 else 0
        val nintendo : Int = if (cbnintendo.isChecked) 1 else 0

        sgenero = findViewById(R.id.sGenero)
        val genero : String = sgenero.selectedItem as String

        if(titulo.isNotEmpty() && descripcion.isNotEmpty() && genero.isNotEmpty()){
            val parametros = mutableMapOf<String, Any?>()

            parametros["id_publicaciones"] = id_publicacion
            parametros["titulo"] = titulo
            parametros["descripcion"] = descripcion
            parametros["xbox"] = xbox.toString()
            parametros["playstation"] = playstation.toString()
            parametros["nintendo"] = nintendo.toString()
            parametros["genero"] = genero
            parametros["nombre_imagen"] = nombre_imagen
            parametros["imagen"] = url_imagen

            val post : JSONObject = JSONObject(parametros)

            modificarPublicacion(post)
        }else{
            Toast.makeText(this,"La publicación esta vacío para actualizar",Toast.LENGTH_SHORT)
        }
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

    private fun modificarPublicacion(post: JSONObject) {
        val queue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_modificar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    val intent = Intent(this, EditarActivity::class.java)

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("id",id_publicacion)

                    startActivity(intent)
                    overridePendingTransition(0, 0)
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