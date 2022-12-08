package com.example.proyectogameverse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PublicacionAdapter : RecyclerView.Adapter<PublicacionHolder>(){
    private lateinit var data : ArrayList<Publicacion>

    init {
        data = ArrayList<Publicacion>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.publicacion_row, parent, false)
        return PublicacionHolder(view)
    }

    override fun onBindViewHolder(holder: PublicacionHolder, position: Int) {
        val publicacion : Publicacion = data.get(position)

        if(publicacion.thumbnail != "") {
            Picasso.get().load(publicacion.thumbnail).into(holder.ivthumbnail)
        }
        holder.tvtitulo.text = publicacion.titulo

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, EditarActivity::class.java)

            intent.putExtra("id",publicacion.id_publicacion)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun guardar(publicacion: Publicacion) {
        data.add(publicacion)
        notifyDataSetChanged()
    }

    fun limpiar() {
        data.clear()
    }
}
