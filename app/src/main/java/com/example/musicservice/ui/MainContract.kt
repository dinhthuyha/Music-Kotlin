package com.example.musicservice.ui

import com.example.musicservice.data.model.Song

interface MainContract {
    interface View{
        fun updateView(song: MutableList<Song>)
    }
    interface Persenter{
        fun handle()
    }
}