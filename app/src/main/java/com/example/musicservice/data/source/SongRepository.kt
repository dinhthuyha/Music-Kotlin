package com.example.musicservice.data.source

import com.example.musicservice.data.model.Song
import com.example.musicservice.data.source.base.OnDataLoaderCallback

class SongRepository() : SongDataSource {
    override fun getList(callback: OnDataLoaderCallback<MutableList<Song>>) {

        var songList = mutableListOf<Song>()
        songList.add(
            Song(
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/df/9f/cb/df9fcb30-b659-4e57-dacb-0dc7924cadbf/mzaf_8162838071707566410.plus.aac.p.m4a"
            )
        )
        songList.add(
            Song("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview114/v4/4d/b9/3d/4db93deb-adc4-dbba-aa8b-e71ce6a99222/mzaf_1431476923263111215.plus.aac.p.m4a")
        )

        if (songList == null)
            callback.onFailure(Exception("Unexpected error"))
        else
            callback.onSuccess(songList)
    }
    companion object{
        private var INSTANCE: SongRepository?=null
        fun getInstance()=
            INSTANCE?: SongRepository().also { INSTANCE=it }
    }
}