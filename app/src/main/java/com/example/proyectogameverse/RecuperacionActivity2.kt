package com.example.proyectogameverse

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

class RecuperacionActivity2 : AppCompatActivity() {
    private var url_recuperar2 : String = "http://192.168.1.87/gameverse_preservidor/usuario/recuperar.php"
    private lateinit var correo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion2)

        configPerfil()

        val bconfirmar : Button = findViewById(R.id.bConfirmar)

        bconfirmar.setOnClickListener{
            verificarCambio()
        }
    }

    private fun configPerfil() {
        if(intent != null){
            correo = intent.getStringExtra("correo").toString()
        }
    }

    private fun verificarCambio() {
        val etpassword : EditText = findViewById(R.id.etPassword2)
        val password : String = etpassword.text.toString()

        val etpassword2 : EditText = findViewById(R.id.etPassword3)
        val password2 : String = etpassword2.text.toString()

        if(password.isNotEmpty() && password2.isNotEmpty()){
            if(password == password2){
                url_recuperar2 += "?correo=$correo&password=$password"
                leerPassword()
            }else{
                Toast.makeText(this,"Las contraseñas introducidas no coinciden",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Introduce la nueva contraseña",Toast.LENGTH_SHORT).show()
        }
    }

    private fun leerPassword() {
        val queue : RequestQueue = Volley.newRequestQueue(this)

        //Log.i("",url_recuperar2)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT,
            url_recuperar2,
            null,
            {
                    response ->
                if(response.getBoolean("exito")){
                    val intent = Intent(this, MainActivity::class.java)

                    startActivity(intent)
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