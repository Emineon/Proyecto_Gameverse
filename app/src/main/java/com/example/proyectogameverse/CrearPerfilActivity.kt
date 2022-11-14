package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CrearPerfilActivity : AppCompatActivity() {
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
                if(crearpassword.isNotEmpty()){
                    if(confpassword.isNotEmpty()){
                        val intent = Intent(this, PrincipalActivity::class.java)

                        startActivity(intent)
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
}