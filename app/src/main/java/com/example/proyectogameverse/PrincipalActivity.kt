package com.example.proyectogameverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        ConfigIU()
    }

    private fun ConfigIU() {
        if(intent != null){

        }
    }
}