package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val bpassword : Button = findViewById(R.id.bConfPass)

        bpassword.setOnClickListener{
            verificarPassword()
        }
    }

    private fun verificarPassword() {
        val etpassword : EditText = findViewById(R.id.etPasswd)
        val password : String = etpassword.text.toString()

        val etpasswordConf : EditText = findViewById(R.id.etPasswdConf)
        val passwordConf : String = etpasswordConf.text.toString()

        if(password.isNotEmpty() && passwordConf.isNotEmpty()){
            if(password == passwordConf){
                Toast.makeText(this,"Son iguales",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"La contraseña no son iguales",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Introduce la nueva contraseña para cambiar",Toast.LENGTH_SHORT).show()
        }
    }
}