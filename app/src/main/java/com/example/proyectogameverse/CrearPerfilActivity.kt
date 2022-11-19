package com.example.proyectogameverse

import android.app.VoiceInteractor
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CrearPerfilActivity : AppCompatActivity() {
    private val url_registrar : String = "http://192.168.1.87/gameverse_preservidor/registrar.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_perfil)

        val bregistrar : Button = findViewById(R.id.bRegistar)

        bregistrar.setOnClickListener{
            verificarRegistro()
        }
    }

    private fun verificarRegistro() {
        val etnombre : EditText = findViewById(R.id.etNombre)
        val nombre : String = etnombre.text.toString()

        val etcorreo : EditText = findViewById(R.id.etCorreo)
        val correo : String = etcorreo.text.toString()

        val etcrearpassword : EditText = findViewById(R.id.etCrearPassword)
        val crearpassword : String = etcrearpassword.text.toString()

        val etconfpassword : EditText = findViewById(R.id.etConfPassword)
        val confpassword : String = etconfpassword.text.toString()

        if(nombre.isNotEmpty()){
            if(correo.isNotEmpty()){
                if(crearpassword.isNotEmpty() && confpassword.isNotEmpty()){
                    if(crearpassword == confpassword){
                        val parametros = mutableMapOf<String, Any?>()
                        parametros["nombre"] = nombre
                        parametros["password"] = crearpassword
                        parametros["email"] = correo

                        val post : JSONObject = JSONObject(parametros)

                        leerRegistro(post)
                    }else{
                        Toast.makeText(this,"No se ingreso igualmente la contraseña", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"Ingresar una contraseña",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Ingresar un correo electronico",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Ingresar un nombre",Toast.LENGTH_SHORT).show()
        }
    }

    private fun leerRegistro(post : JSONObject) {
        val quece = Volley.newRequestQueue(this)

        //Log.i("",post.getString("nombre"))

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_registrar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    val intent = Intent(this, MenuPrincipalActivity::class.java)

                    startActivity(intent)
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()
                }
            },
            {
                errorResponse ->
                Toast.makeText(applicationContext,"Error en el registro",Toast.LENGTH_SHORT).show()
            }
        )

        quece.add(request)
    }
}