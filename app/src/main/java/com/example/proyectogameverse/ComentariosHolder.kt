package com.example.proyectogameverse

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComentariosHolder : RecyclerView.ViewHolder{
    lateinit var ivicono : ImageView
    lateinit var tvnombre : TextView
    lateinit var tvfecha : TextView
    lateinit var tvcomentario : TextView

    constructor(itemView : View) : super(itemView){
        ivicono = itemView.findViewById(R.id.ivIcono)
        tvnombre = itemView.findViewById(R.id.tvNombreUser)
        tvfecha = itemView.findViewById(R.id.tvFechaComentario)
        tvcomentario = itemView.findViewById(R.id.tvComentario)
    }
}
