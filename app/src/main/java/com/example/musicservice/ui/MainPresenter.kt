package com.example.musicservice.ui

import com.example.musicservice.data.model.Song
import com.example.musicservice.data.source.SongDataSource
import com.example.musicservice.data.source.SongRepository
import com.example.musicservice.data.source.base.OnDataLoaderCallback

class MainPresenter(
    private val view: MainContract.View,
    private val repository: SongDataSource
) : MainContract.Persenter {
    override fun handle() {
        repository.getList(object : OnDataLoaderCallback<MutableList<Song>>{
            override fun onSuccess(data: MutableList<Song>) {
                view.updateView(data)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }

        })
    }
}