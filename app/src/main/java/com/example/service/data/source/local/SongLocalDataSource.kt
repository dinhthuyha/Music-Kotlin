package com.example.service.data.source.local

import android.content.Context
import android.util.Log
import com.example.service.data.model.Song
import com.example.service.data.source.remote.SongDataSource

class SongLocalDataSource( var context: Context) : SongDataSource{
    private  val TAG = "SongLocalDataSource"
    override fun getAllSong(): MutableList<Song> {
        Log.d(TAG, "getAllSong: "+SongProvider.getSongList(context))
        return SongProvider.getSongList(context)
    }
    companion object {
        @Volatile
        private var INSTANCE: SongLocalDataSource? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SongLocalDataSource(context)
            }
    }
}