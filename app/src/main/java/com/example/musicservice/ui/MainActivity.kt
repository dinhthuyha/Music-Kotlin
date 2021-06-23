package com.example.musicservice.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicservice.R
import com.example.musicservice.Song
import com.example.musicservice.service.MyService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var songList = mutableListOf<Song>()
    private val TAG = "MainActivity"

    private lateinit var mService: MyService
    private var boundService = false
    private var playIntent: Intent? = null
    private var click = false
    private var adapter= adapter()

    var connService = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyService.MusicBinder
            mService = binder.getService()
            boundService = true
            mService.setList(songList)


        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundService = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bindView
        initView()

    }

    private fun initView() {
        songList.add(
            Song(
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/df/9f/cb/df9fcb30-b659-4e57-dacb-0dc7924cadbf/mzaf_8162838071707566410.plus.aac.p.m4a"
            )
        )
        songList.add(
            Song("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview114/v4/4d/b9/3d/4db93deb-adc4-dbba-aa8b-e71ce6a99222/mzaf_1431476923263111215.plus.aac.p.m4a")
        )
        rv.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter.updateData(songList)
        rv.adapter = adapter.apply {
            onItemClick = { item, position ->

                //Log.d(TAG, "onServiceConnected: " + item.url)
                mService.playSong(position)


            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also { intent ->
            bindService(
                intent,
                connService,
                Context.BIND_AUTO_CREATE
            )
            startService(intent)
        }

    }
}