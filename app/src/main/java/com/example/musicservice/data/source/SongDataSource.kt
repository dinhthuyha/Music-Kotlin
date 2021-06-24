package com.example.musicservice.data.source

import com.example.musicservice.data.model.Song
import com.example.musicservice.data.source.base.OnDataLoaderCallback

interface SongDataSource {
    fun getList( callback: OnDataLoaderCallback<MutableList<Song>>)
}