package com.example.service.data.source.remote

import com.example.service.data.model.Song

interface SongDataSource {
    fun getAllSong():MutableList<Song>
}