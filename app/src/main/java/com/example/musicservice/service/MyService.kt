package com.example.musicservice.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.example.musicservice.data.model.Song

class MyService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    private val TAG = "MyService"
    private var player: MediaPlayer? = null

    //list song
    private var songs: MutableList<Song>? = null
    private var songPostion = 0
    private val musicBinder: IBinder = MusicBinder()

    override fun onCreate() {
        super.onCreate()
        /**
         * create media player
         * khoi tao vi tri bai hat
         */
        player = MediaPlayer()
        songPostion = 0
        initMusicPlayer()
    }


    override fun onBind(intent: Intent): IBinder {
        return musicBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        player?.let {
            it.stop()
            it.release()
        }
        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        //start playback
        mp?.let { it.start() }

    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.d(TAG, "onError: ")
        mp?.reset()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //check xem da phat den cuoi danh sach chua
        player?.let {
            if (it.currentPosition > 0)
                mp?.reset()
            playNext()

        }

    }

    public fun playSong(pos: Int) {
        //play
        songPostion = pos
        player!!.reset()
        var playSong = songs?.get(songPostion)

        player?.let {
            it.setDataSource(applicationContext, Uri.parse(playSong?.url.toString()))
            it.prepareAsync()
        }

    }

    fun pause(pos: Int) {
        player!!.pause()
    }

    private fun playNext() {
        songPostion++
        if (songPostion >= songs!!.size) {
            songPostion = 0
        }
        playSong(songPostion)

    }

    public fun playPre() {
        songPostion--
        if (songPostion < 0) songPostion = songs!!.size - 1
        playSong(songPostion)

    }

    fun pausePlayer() = player?.pause()

    fun setList(theSongs: MutableList<Song>) {
        songs = theSongs
    }

    private fun initMusicPlayer() {
        /**
         * set cac thuoc tinh can thiet cho trinh phat nhac
         *
         */
        player = MediaPlayer().apply {
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnPreparedListener(this@MyService);
            setOnCompletionListener(this@MyService);
            setOnErrorListener(this@MyService);

        }

    }

    inner class MusicBinder : Binder() {
        fun getService(): MyService = this@MyService

    }

}