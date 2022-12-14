package com.example.proyectogameverse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

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
            if(busqueda.thumbnail != ""){
                Picasso.get().load(busqueda.thumbnail).into(holder.ivthumbnail)
            }else{
                holder.ivthumbnail.setImageResource(R.drawable.inc_videojuego)
            }
        }

        if(busqueda.id_grupos != 0){
            holder.tvtitulo.text = busqueda.nombre_grupo
            if(busqueda.thumbnail != ""){
                Picasso.get().load(busqueda.thumbnail).into(holder.ivthumbnail)
            }
        }

        if(busqueda.id_publicacion == 0 && busqueda.id_perfil != 0){
            holder.tvtitulo.text = busqueda.nombre
            if(busqueda.thumbnail != ""){
                Picasso.get().load(busqueda.thumbnail).into(holder.ivthumbnail)
            }/*else{
                holder.ivthumbnail.setImageDrawable(null)
                holder.ivthumbnail.setBackgroundResource(R.drawable.ic_perfil)
            }*/
        }

        holder.itemView.setOnClickListener{
            if(busqueda.id_publicacion != 0){
                val intent = Intent(holder.itemView.context, PublicacionUserActivity::class.java)

                intent.putExtra("id",busqueda.id_publicacion)
                intent.putExtra("titulo",busqueda.titulo)
                intent.putExtra("descripcion",busqueda.descripcion)
                intent.putExtra("creacion",busqueda.creacion)
                intent.putExtra("imagen",busqueda.thumbnail)
                intent.putExtra("usuario",busqueda.nombre)

                intent.putExtra("id_usuario",busqueda.id_usuario)

                holder.itemView.context.startActivity(intent)
            }

            if(busqueda.id_grupos != 0){
                val intent = Intent(holder.itemView.context, VerGrupoActivity::class.java)

                intent.putExtra("id",busqueda.id_grupos)
                intent.putExtra("nombre",busqueda.nombre_grupo)
                intent.putExtra("descripcion",busqueda.descripcion_grupo)
                intent.putExtra("icono",busqueda.thumbnail)

                intent.putExtra("id_usuario",busqueda.id_usuario)

                holder.itemView.context.startActivity(intent)
            }

            if(busqueda.id_perfil != 0){
                val intent = Intent(holder.itemView.context, PerfilActivity::class.java)

                intent.putExtra("id",busqueda.id_perfil)
                intent.putExtra("nombre",busqueda.nombre)
                intent.putExtra("email",busqueda.email)
                intent.putExtra("descripcion",busqueda.descripcion_perfil)
                intent.putExtra("videojuego",busqueda.videojuego)
                intent.putExtra("imagen",busqueda.thumbnail)

                intent.putExtra("id_usuario",busqueda.id_usuario)

                holder.itemView.context.startActivity(intent)
            }
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