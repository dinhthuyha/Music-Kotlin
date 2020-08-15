package com.example.service.data.source.remote

import android.util.Log
import com.example.service.data.model.Song

class SongRepository( private val localDataSource: SongDataSource): SongDataSource {
    private  val TAG = "SongRepository"
    override fun getAllSong(): MutableList<Song> {
        Log.d(TAG, "getAllSong: ${localDataSource.getAllSong().size}")
        return localDataSource.getAllSong()
    }

    companion object {
        @Volatile
        private var INSTANCE: SongRepository? = null

        fun getInstance(localDataSource: SongDataSource) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SongRepository(localDataSource)
            }
    }
}