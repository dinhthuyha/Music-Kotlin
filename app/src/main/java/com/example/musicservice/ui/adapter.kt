package com.example.musicservice.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.R
import com.example.musicservice.data.model.Song

class adapter() : RecyclerView.Adapter<ViewHolder>() {
    var onItemClick: (Song, Int) -> Unit = { _, _ -> }
    private val songs = mutableListOf<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(songs[position])
    }

    override fun getItemCount(): Int= songs.size
    fun updateData(newPhotos: List<Song>){
        songs.clear()
        songs.addAll(newPhotos)
        notifyDataSetChanged()
    }

}