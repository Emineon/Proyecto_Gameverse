package com.example.proyectogameverse

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var url_login : String = ""

    private lateinit var perfil : String

    private var nombre : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etperfil : EditText = findViewById(R.id.etPerfil)
        val etpassword : EditText = findViewById(R.id.etPassword)

        etpassword.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (etperfil.text.isNotEmpty() && etpassword.text.isNotEmpty()){
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(etperfil.text.toString(),
                            etpassword.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful){
                                verificarPerfil()
                            }else {
                                Toast.makeText(this,"No se ha producido un error autenticando el correo",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            false
        })


        val biniciar : Button = findViewById(R.id.bIniciar)

        biniciar.setOnClickListener{
            if (etperfil.text.isNotEmpty() && etpassword.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etperfil.text.toString(),
                        etpassword.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            verificarPerfil()
                        }else {
                            Toast.makeText(this,"No se ha producido un error autenticando el correo",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val bolvidar : TextView = findViewById(R.id.bOlvidar)

        bolvidar.setOnClickListener{
            val intent = Intent(this, RecuperacionActivity::class.java)

            startActivity(intent)
        }

        val bregistrar : Button = findViewById(R.id.bRegistrar)

        bregistrar.setOnClickListener{
            val intent = Intent(this, CrearPerfilActivity::class.java)

            startActivity(intent)
        }
    }

    private fun verificarPerfil() {
        val etperfil : EditText = findViewById(R.id.etPerfil)
        perfil = etperfil.text.toString()

        val etpassword : EditText = findViewById(R.id.etPassword)
        val password : String = etpassword.text.toString()

        url_login = "http://3.22.175.225/gameverse_servidor/usuario/login.php?nombre=$perfil&password=$password"

        leerAcceso()
    }

    private fun leerAcceso() {
        val queue : RequestQueue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url_login,
            null,
            {
                response ->
                if(response.getBoolean("exito")){
                    obtenerUsuario(response.getJSONArray("usuario"))
                }else{
                    val mensaje : String = response.getString("mensaje")
                    Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()
                }
            },
            {
                errorResponse ->
                Toast.makeText(this,"Error en el acceso a sistema",Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun obtenerUsuario(usuario : JSONArray) {
        for(i in 0 .. usuario.length() - 1){
            val perfil = usuario[i] as JSONObject

            nombre = perfil.getString("nombre")
        }

        val intent = Intent(this, MenuPrincipalActivity::class.java)
        intent.putExtra("nombre",nombre)

        startActivity(intent)
    }
}