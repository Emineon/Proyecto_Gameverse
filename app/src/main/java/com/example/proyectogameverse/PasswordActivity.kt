package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PasswordActivity : AppCompatActivity() {
    private var id_perfil : Int = 0
    private var url_actualizar = "http://3.22.175.225/gameverse_servidor/usuario/actualizar.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val bpassword : Button = findViewById(R.id.bConfPass)

        bpassword.setOnClickListener{
            verificarPassword()
        }

        val etpasswordConf : EditText = findViewById(R.id.etPasswdConf)
        etpasswordConf.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                verificarPassword()
            }
            false
        })
    }

    override fun onStart() {
        super.onStart()

        ConfigUI()
    }

    private fun ConfigUI(){
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")){
            id_perfil = intent.getIntExtra("id_perfil",0)
        }
    }

    private fun verificarPassword() {
        val etpassword : EditText = findViewById(R.id.etPasswd)
        val password : String = etpassword.text.toString()

        val etpasswordConf : EditText = findViewById(R.id.etPasswdConf)
        val passwordConf : String = etpasswordConf.text.toString()

        if(password.isNotEmpty() && passwordConf.isNotEmpty()){
            if(password == passwordConf){
                val parametros = mutableMapOf<String, Any?>()

                parametros["id"] = id_perfil
                parametros["accion"] = "Password"
                parametros["password"] = password

                val post : JSONObject = JSONObject(parametros)

                actualizarPassword(post)
            }else{
                Toast.makeText(this,"La contraseña no son iguales",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Introduce la nueva contraseña para cambiar",Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarPassword(post: JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_actualizar,
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
                Toast.makeText(applicationContext, "Error en el acceso de sistema", Toast.LENGTH_SHORT).show()
            }
        )

        quece.add(request)
    }
}