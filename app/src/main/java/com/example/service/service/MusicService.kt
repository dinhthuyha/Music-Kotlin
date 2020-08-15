package com.example.service.service

import android.app.PendingIntent
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.service.R
import com.example.service.data.model.Song
import com.example.service.ui.MainActivity
import java.util.ArrayList

class MusicService : Service(), MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    //media player
    private var player: MediaPlayer? = null
    //song list
    private var songs: MutableList<Song>? = null
    //current position
    private var songPosn = 0
    //binder
    private val musicBind: IBinder = MusicBinder()
    //title of current song
    private var songTitle:String?=null
    override fun onCreate() {
        //create the service
        super.onCreate()
        //initialize position
        songPosn = 0
        //random
        //create player
        player = MediaPlayer()
        //initialize
        initMusicPlayer()
    }

    fun initMusicPlayer() {
        //set player properties
        player?.setWakeMode(
            applicationContext,
            PowerManager.PARTIAL_WAKE_LOCK
        )
        player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        //set listeners
        player?.setOnPreparedListener(this)
        player?.setOnCompletionListener(this)
        player?.setOnErrorListener(this)
    }

    //pass song list
    fun setList(theSongs: MutableList<Song>?) {
        songs = theSongs
    }

    //binder
    inner class MusicBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    //activity will bind to service
    override fun onBind(intent: Intent): IBinder? {
        return musicBind
    }

    //release resources when unbind
    override fun onUnbind(intent: Intent): Boolean {
        player?.stop()
        player?.release()
        return false
    }

    //play a song
    fun playSong() {
        player?.reset()
        val playSong = songs?.get(songPosn)
        songTitle = playSong?.title
        val currSong: Long? = playSong?.id

        //set the data source
        try {
            val trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong!!
            )
            player?.setDataSource(applicationContext, trackUri)
        } catch (e: Exception) {
            Log.e("MUSIC SERVICE", "Error setting data source", e)
        }
        player?.prepareAsync()
    }

    //set the song
    fun setSong(songIndex: Int) {
        songPosn = songIndex
    }

    override fun onCompletion(mp: MediaPlayer) {
        //check if playback has reached the end of a track
        if (player?.currentPosition!! > 0) {
            mp.reset()
            playNext()
        }
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Log.v("MUSIC PLAYER", "Playback Error")
        mp.reset()
        return false
    }

    override fun onPrepared(mp: MediaPlayer) {
        //start playback
        mp.start()
        //notification
        val notIntent = Intent(this, MainActivity::class.java)
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendInt = PendingIntent.getActivity(
            this, 0,
            notIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this)
        builder.setContentIntent(pendInt)
            .setSmallIcon(R.drawable.play)
            .setTicker(songTitle)
            .setOngoing(true)
            .setContentText(songTitle)
        val not = builder.build()
        startForeground(NOTIFY_ID, not)
    }

    //playback methods
    val posn: Int?
        get() = player?.currentPosition

    val dur: Int?
        get() = player?.duration

    val isPng: Boolean?
        get() = player?.isPlaying

    fun pausePlayer() {
        player?.pause()
    }

    fun seek(posn: Int) {
        player?.seekTo(posn)
    }

    fun go() {
        player?.start()
    }

    //skip to previous track
    fun playPrev() {
        songPosn--
        if (songPosn < 0) songPosn = songs?.size!! - 1
        playSong()
    }

    //skip to next
    fun playNext() {
        songPosn++
        if (songPosn >= songs?.size!!) songPosn = 0
        playSong()
    }

    override fun onDestroy() {
        stopForeground(true)
    }

    companion object {
        //notification id
        private const val NOTIFY_ID = 1
    }

}
