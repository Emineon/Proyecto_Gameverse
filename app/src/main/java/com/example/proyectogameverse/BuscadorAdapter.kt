package com.example.proyectogameverse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BuscadorAdapter : RecyclerView.Adapter<BuscadorHolder>() {
    private lateinit var data : ArrayList<Buscar>

    init{
        data = ArrayList<Buscar>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuscadorHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.publicacion_row, parent, false)
        return BuscadorHolder(view)
    }

    override fun onBindViewHolder(holder: BuscadorHolder, position: Int) {
        val busqueda : Buscar = data.get(position)

        if(busqueda.id_publicacion != 0){
            holder.tvtitulo.text = busqueda.titulo
        }

        if(busqueda.id_grupos != 0){
            holder.tvtitulo.text = busqueda.nombre_grupo
        }

        if(busqueda.id_perfil != 0){
            holder.tvtitulo.text = busqueda.nombre
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun guardar(buscar: Buscar) {
        data.add(buscar)
        notifyDataSetChanged()
    }

    fun limpiar() {
        data.clear()
    }
}