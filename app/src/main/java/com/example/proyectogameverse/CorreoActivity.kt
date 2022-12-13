package com.example.proyectogameverse

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CorreoActivity : AppCompatActivity() {
    private var url_actualizar = "http://3.22.175.225/gameverse_servidor/usuario/actualizar.php"

    private var id_perfil : Int = 0
    private var nombre : String = ""
    private var correo : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correo)

        val bconfirmar : Button = findViewById(R.id.bConfirmarMail)
        bconfirmar.setOnClickListener{
            verificarCorreo()
        }

        val etpassword : EditText = findViewById(R.id.etPasswdMail)
        etpassword.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                verificarCorreo()
            }
            false
        })
    }

    override fun onStart() {
        super.onStart()

        ConfigIU()
    }

    private fun ConfigIU() {
        val intent = intent
        if(intent != null && intent.hasExtra("id_perfil")){
            id_perfil = intent.getIntExtra("id_perfil",0)
            nombre = intent.getStringExtra("nombre").toString()
            correo = intent.getStringExtra("correo").toString()

            val etemail : EditText = findViewById(R.id.etEmail)
            etemail.setText(correo)
        }
    }

    private fun verificarCorreo() {
        val etemail : EditText = findViewById(R.id.etEmail)
        val nuevocorreo = etemail.text.toString()

        val etpassword : EditText = findViewById(R.id.etPasswdMail)
        val passwd : String = etpassword.text.toString()

        val parametros = mutableMapOf<String, Any?>()

        parametros["id"] = id_perfil
        parametros["accion"] = "Correo"
        parametros["nombre"] = nombre
        parametros["password"] = passwd
        parametros["correo"] = nuevocorreo

        val post : JSONObject = JSONObject(parametros)

        if(nuevocorreo.isNotEmpty()){
            if(passwd.isNotEmpty()){
                mensajeConfirmacion(post)
            }else{
                Toast.makeText(this,"Confirmar la contraseña para realizar el cambio", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Vuelve a introducir el correo electrónico",Toast.LENGTH_SHORT).show()
        }
    }

    private fun mensajeConfirmacion(post : JSONObject) {
        val alert : AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        actualizarCorreo(post)
                    })
                setNegativeButton(R.string.cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }

            builder.setTitle("¿Quieres guardar los cambios?")

            builder.create()
            builder.show()
        }
    }

    private fun actualizarCorreo(post: JSONObject) {
        val quece = Volley.newRequestQueue(this)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url_actualizar,
            post,
            {
                response ->
                if(response.getBoolean("exito")){
                    val intent = Intent(this,AjustesActivity::class.java)
                    intent.putExtra("id_perfil",id_perfil)
                    intent.putExtra("nombre",nombre)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
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