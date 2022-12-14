package com.example.proyectogameverse

import android.app.VoiceInteractor
import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class CrearPerfilActivity : AppCompatActivity() {
    private val url_registrar : String = "http://3.22.175.225/gameverse_servidor/usuario/registrar.php"

    private var nombre : String = ""

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_perfil)

        firebaseAuth = Firebase.auth

        val bregistrar : Button = findViewById(R.id.bRegistar)

        bregistrar.setOnClickListener{
            verificarRegistro()
        }
    }

    override fun onStart() {
        super.onStart()

        val user = firebaseAuth.currentUser
        if(user != null){
            if(user.isEmailVerified){

            }
        }
    }

    private fun verificarRegistro() {
        val etnombre : EditText = findViewById(R.id.etNombre)
        nombre = etnombre.text.toString()

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
                        firebaseAuth.createUserWithEmailAndPassword(correo, crearpassword).addOnCompleteListener(this) { task ->
                            if(!task.isSuccessful) {
                                try {
                                    throw task.exception!!
                                } catch(e: FirebaseAuthUserCollisionException) {
                                    Toast.makeText(applicationContext, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                                } catch(e: Exception) {
                                    Toast.makeText(applicationContext, "Ha ocurrido un error al registrar el usuario.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val user = firebaseAuth.currentUser
                                val userId = user!!.uid
                                val userEmail = user.email

                                val parametros = mutableMapOf<String, Any?>()
                                parametros["nombre"] = nombre
                                parametros["password"] = crearpassword
                                parametros["email"] = userEmail

                                val post : JSONObject = JSONObject(parametros)

                                user.sendEmailVerification().addOnCompleteListener { secondTask ->
                                    if(secondTask.isSuccessful) {
                                        Log.d("", "Email de verificación enviado a $userEmail.")
                                        enviarRegistro(post)
                                    } else {
                                        Log.d("", "Error al enviar email de verificación a $userEmail.")
                                    }
                                }
                            }
                        }
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

    private fun enviarRegistro(post : JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_registrar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    val intent = Intent(this, MenuPrincipalActivity::class.java)
                    intent.putExtra("nombre",nombre)

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
}