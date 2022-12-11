package com.example.proyectogameverse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ComentariosAdapter : RecyclerView.Adapter<ComentariosHolder>() {
    private lateinit var data : ArrayList<Comentarios>

    init{
        data = ArrayList<Comentarios>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentariosHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.comentarios_row, parent, false)
        return ComentariosHolder(view)
    }

    override fun onBindViewHolder(holder: ComentariosHolder, position: Int) {
        val comments : Comentarios = data.get(position)

        holder.tvnombre.text = comments.nombre_perfil
        holder.tvfecha.text = comments.creacion
        holder.tvcomentario.text = comments.comentario

        if(comments.icono != ""){
            holder.ivicono.setBackgroundDrawable(null)
            Picasso.get().load(comments.icono).into(holder.ivicono)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun guardar(comments: Comentarios) {
        data.add(comments)
        notifyDataSetChanged()
    }

    fun limpiar() {
        data.clear()
    }
}