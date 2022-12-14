package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class CrearGrupoActivity : AppCompatActivity() {
    private var url_crear : String = "http://3.22.175.225/gameverse_servidor/grupos/crear.php"

    private var id_perfil : Int = 0
    private var url_imagen : String = ""

    private var nombre_imagen : String = ""

    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_grupo)

        storage = Firebase.storage

        val bsubirgrupo : Button = findViewById(R.id.bSubirGrupo)

        bsubirgrupo.setOnClickListener{
            seleccionarImagen()
        }

        val cancelar_imagen : ImageView = findViewById(R.id.cancelar_subidaGrupo)

        cancelar_imagen.setOnClickListener{
            borrarImagen()
        }
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

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
            type = "image/*"
        }

        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 1){
            if(data != null) {
                val uri: Uri? = data.data

                cargaImagen(uri)
            }
        }
    }

    private fun cargaImagen(uri: Uri?) {
        if(uri != null){
            val uri_guardado = uri
            nombre_imagen = generarNombreArchivo()

            mostrarImagen(nombre_imagen)

            Toast.makeText(this,"La imagen se esta cargando",Toast.LENGTH_SHORT).show()

            subirImagen(uri_guardado)
        }
    }

    private fun subirImagen(uri_guardado: Uri) {
        var storageRef = storage.getReference("iconos/${nombre_imagen}")
        var carga = storageRef.putFile(uri_guardado)

        carga.addOnFailureListener{ error ->
            Toast.makeText(this,"Ocurrio un error mientras se cargaba la imagen",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener{ task ->
            storageRef.downloadUrl.addOnSuccessListener{ it
                url_imagen = it.toString()
                Toast.makeText(this,"Se cargo correctamente la imagen",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generarNombreArchivo(): String {
        val time = Calendar.getInstance().time
        val formatime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
        val current = formatime.format(time)
        return "img_$current"
    }

    private fun mostrarImagen(nombre_imagen : String) {
        val tvimagen : TextView = findViewById(R.id.tvImagenGrupo)
        tvimagen.setText(nombre_imagen)

        val cancelar_subida : ImageView = findViewById(R.id.cancelar_subidaGrupo)
        cancelar_subida.visibility = View.VISIBLE
    }

    private fun borrarImagen() {
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        var storageRef = storage.reference
                        var eliminar = storageRef.child("iconos/${nombre_imagen}")

                        eliminar.delete().addOnSuccessListener{
                            url_imagen = ""

                            Toast.makeText(this@CrearGrupoActivity,"La imagen quedo eliminada",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(this@CrearGrupoActivity,"Ocurrio un error para dicha acción",Toast.LENGTH_SHORT).show()
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