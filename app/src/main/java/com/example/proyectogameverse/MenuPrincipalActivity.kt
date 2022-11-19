package com.example.proyectogameverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.chip.Chip

class MenuPrincipalActivity : AppCompatActivity() {
    private  val url_perfil : String = "http://192.168.1.87/gameverse_preservidor/perfil.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        val chippublicaciones : Chip = findViewById(R.id.chipPublicaciones)

        chippublicaciones.setOnClickListener{
            val intent = Intent(this, PublicacionesActivity::class.java)

            startActivity(intent)
        }

        val chipgrupos : Chip = findViewById(R.id.chipGrupos)

        chipgrupos.setOnClickListener{
            val intent = Intent(this, GruposActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuprincipal,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.opc_buscar -> {
                dirigirBusqueda()
            }
            R.id.opc_config -> {
                dirigirAjustes()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun dirigirBusqueda() {
        val intent = Intent(this, BuscadorActivity::class.java)

        startActivity(intent)
    }

    private fun dirigirAjustes() {
        val intent = Intent(this, AjustesActivity::class.java)

        startActivity(intent)
    }
}