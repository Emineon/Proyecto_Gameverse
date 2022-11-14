package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        //ConfigIU()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuprincipal,menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*private fun ConfigIU() {
        if(intent != null){

        }
    }*/
}