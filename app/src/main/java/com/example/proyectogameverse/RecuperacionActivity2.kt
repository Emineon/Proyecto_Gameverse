package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RecuperacionActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion2)

        val bconfirmar : Button = findViewById(R.id.bConfirmar)

        bconfirmar.setOnClickListener{
            verificarCambio()
        }
    }

    private fun verificarCambio() {
        val etpassword : EditText = findViewById(R.id.etPassword2)
        val password : String = etpassword.text.toString()

        val etpassword2 : EditText = findViewById(R.id.etPassword3)
        val password2 : String = etpassword2.text.toString()

        if(password.isNotEmpty() && password2.isNotEmpty()){
            if(password == password2){
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
            }else{
                Toast.makeText(this,"Las contraseñas introducidas no coinciden",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Introduce la nueva contraseña",Toast.LENGTH_SHORT).show()
        }
    }
}