package com.example.service.ui

import com.example.service.data.source.remote.SongDataSource

class MainPresenter(private val view: MainContract.View, private val songRepo: SongDataSource) : MainContract.Presenter {

    override fun load() {
        songRepo.getAllSong().run {
            if (isNotEmpty()) {
                view.updateRecyclerView(this)
            }
        }
    }
}
