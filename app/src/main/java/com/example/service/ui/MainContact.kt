package com.example.service.ui

import com.example.service.data.model.Song
interface MainContract {
    interface View {
        fun updateRecyclerView(list: List<Song>)

    }

    interface Presenter {
        fun load()
    }
}