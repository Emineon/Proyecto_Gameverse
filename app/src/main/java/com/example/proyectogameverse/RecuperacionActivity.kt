package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class RecuperacionActivity : AppCompatActivity() {
    private var url_recuperar : String = ""
    private lateinit var perfil : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion)

        val brecuperar : Button = findViewById(R.id.bRecuperar)

        brecuperar.setOnClickListener{
            verificarPerfil()
        }
    }

    private fun verificarPerfil() {
        val etperfil : EditText = findViewById(R.id.etPerfil2)
        perfil = etperfil.text.toString()

        if(perfil.isNotEmpty()){
            url_recuperar = "http://192.168.1.87/gameverse_preservidor/usuario/recuperar.php?correo=$perfil"
            leerPerfil()
        }else{
            Toast.makeText(this,"Introduce el correo electrónico",Toast.LENGTH_SHORT).show()
        }
    }

    private fun leerPerfil() {
        val queue : RequestQueue = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url_recuperar,
            null,
            {
                response ->
                if(response.getBoolean("exito")){
                    val intent = Intent(this, RecuperacionActivity2::class.java)
                    intent.putExtra("correo",perfil)

                    startActivityForResult(intent,1)
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
}