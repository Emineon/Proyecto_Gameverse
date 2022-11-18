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
    private var url_recuperar : String = "http://192.168.1.87/gameverse_preservidor/recuperar.php"

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
        val perfil : String = etperfil.text.toString()

        if(perfil.isNotEmpty()){
            url_recuperar += "?correo=$perfil"
            leerPerfil()
            val intent = Intent(this,RecuperacionActivity2::class.java)

            startActivity(intent)
        }else{
            Toast.makeText(this,"Introduce el nombre o correo electrónico",Toast.LENGTH_SHORT).show()
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

                    startActivityForResult(intent,1)
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