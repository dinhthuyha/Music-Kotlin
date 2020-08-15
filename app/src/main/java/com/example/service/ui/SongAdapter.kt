package com.example.service.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.service.R
import com.example.service.data.model.Song
import kotlinx.android.synthetic.main.song.view.*

class SongAdapter(val listSong: List<Song>, listener: SongClick) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    val songClickListener: SongClick = listener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = listSong[position]
        holder.bindData(s)
        holder.itemView.setOnClickListener {
            songClickListener.onSongClick(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(song: Song) {
            itemView.song_title.text = song.title
            itemView.song_artist.text = song.artist

        }

    }

    interface SongClick {
        //fun onSongClick(song:Song)
        fun onSongClick(index: Int)
    }

}