package com.example.musicservice.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.Song
import kotlinx.android.synthetic.main.item.view.*

class ViewHolder(
    itemView: View,
    onItemClick: (Song, Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private var itemData:Song ?=null
    init {
        itemView.setOnClickListener { itemData?.let { onItemClick(it, adapterPosition) } }
    }

    fun bindData(model: Song){
        itemData= model
        itemView.run {
            textView.text="bai so 1"
        }
    }
}