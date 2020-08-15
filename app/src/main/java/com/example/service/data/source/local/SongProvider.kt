package com.example.service.data.source.local

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.example.service.data.model.Song

object SongProvider {

    fun getSongList(context: Context): MutableList<Song> {
        var songList: MutableList<Song> = ArrayList()
        //query external audio
        val musicResolver: ContentResolver = context.contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor =
            musicResolver.query(musicUri, null, null, null, null)
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            val titleColumn =
                musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn =
                musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val artistColumn =
                musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            //add songs to list
            do {
                val thisId = musicCursor.getLong(idColumn)
                val thisTitle = musicCursor.getString(titleColumn)
                val thisArtist = musicCursor.getString(artistColumn)
                songList.add(Song(thisId, thisTitle, thisArtist))
            } while (musicCursor.moveToNext())
        }
        return songList
    }
}