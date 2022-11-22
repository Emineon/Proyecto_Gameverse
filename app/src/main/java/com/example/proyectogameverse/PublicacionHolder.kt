package com.example.proyectogameverse

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PublicacionHolder : RecyclerView.ViewHolder{
    lateinit var ivthumbnail : ImageView
    lateinit var tvtitulo : TextView

    constructor(itemView : View) : super(itemView){
        ivthumbnail = itemView.findViewById(R.id.ivThumbnail)
        tvtitulo = itemView.findViewById(R.id.tvTitulo)
    }
}
