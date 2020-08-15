package com.example.service.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.service.R
import com.example.service.data.source.remote.SongRepository
import com.example.service.data.source.local.SongProvider
import com.example.service.data.model.Song
import com.example.service.data.source.local.SongLocalDataSource
import com.example.service.service.MusicController
import com.example.service.service.MusicService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View, SongAdapter.SongClick,
    MediaController.MediaPlayerControl {
    private var presenter: MainContract.Presenter? = null
    private var songList: MutableList<Song> = ArrayList()
    private val TAG = "MainActivity"
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    //service
    private var musicSrv: MusicService? = null
    private var playIntent: Intent? = null
    //binding
    private var musicBound = false
    //controller
    private var controller: MusicController? = null
    //activity and playback pause flags
    private var paused = false  //activity and playback pause flags
    private var playbackPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(
            this,
            SongRepository.getInstance(SongLocalDataSource.getInstance(this))
        )
        initPermission()
        setController()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size == 1 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                presenter?.load()
                Toast.makeText(this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Toast.makeText(this, "Permission isn't granted ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Permisson don't granted and dont show dialog again ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Register permission
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else
                presenter?.load()
        }
    }

    //user song select
    override fun onSongClick(index: Int) {
        musicSrv!!.setSong(index)
        Log.d("A", "songPicked: $index")
        musicSrv!!.playSong()
        if (playbackPaused) {
            setController()
            playbackPaused = false
        }
        controller!!.show(0)

    }

    override fun onStart() {
        super.onStart()
        if (playIntent == null) {
            playIntent = Intent(this, MusicService::class.java)
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
            startService(playIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (paused) {
            setController()
            paused = false
        }
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun onStop() {
        controller!!.hide()
        super.onStop()
    }

    override fun onDestroy() {
        stopService(playIntent)
        musicSrv = null
        super.onDestroy()
    }

    //connect to the service
    private val musicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: MusicService.MusicBinder = service as MusicService.MusicBinder
            //get service
            musicSrv = binder.service
            Log.d("AA", "onServiceConnected: ")
            //pass list
            musicSrv!!.setList(songList)
            musicBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            musicBound = false
        }
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return 0
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun getCurrentPosition(): Int {
        return if (musicSrv != null && musicBound && musicSrv?.isPng!!) musicSrv!!.posn!! else 0
    }

    override fun getDuration(): Int {
        return if (musicSrv != null && musicBound && musicSrv?.isPng!!) musicSrv?.dur!! else 0
    }

    override fun isPlaying(): Boolean {
        return if (musicSrv != null && musicBound) musicSrv?.isPng!! else false
    }

    override fun pause() {
        playbackPaused = true
        musicSrv?.pausePlayer()
    }

    override fun seekTo(pos: Int) {
        musicSrv?.seek(pos)
    }

    override fun start() {
        musicSrv?.go()
    }

    //set the controller up
    private fun setController() {
        controller = MusicController(this)
        //set previous and next button listeners
        controller!!.setPrevNextListeners(
            { playNext() }
        ) { playPrev() }
        //set and show
        controller!!.setMediaPlayer(this)
        controller!!.setAnchorView(rv)
        controller!!.isEnabled = true
    }

    private fun playNext() {
        musicSrv?.playNext()
        if (playbackPaused) {
            setController()
            playbackPaused = false
        }
        controller?.show(0)
    }

    private fun playPrev() {
        musicSrv?.playPrev()
        if (playbackPaused) {
            setController()
            playbackPaused = false
        }
        controller?.show(0)
    }


    override fun updateRecyclerView(list: List<Song>) {
        Log.d("main", "updateRecyclerView: "+list.size)
        songList=list as MutableList<Song>
        rv.layoutManager = LinearLayoutManager(this )
        rv.adapter = SongAdapter(list, this)
    }

}