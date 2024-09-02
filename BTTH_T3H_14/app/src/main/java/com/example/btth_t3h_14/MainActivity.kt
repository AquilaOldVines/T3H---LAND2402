package com.example.btth_t3h_14

import musicview.ListMusicView
import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btth_t3h_14.databinding.ActivityMainBinding
import core.MainActivityViewModel
import core.MusicRepository
import data.Song
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), ListMusicView.PhatBaiMoi {

    private val viewModel by viewModels<MainActivityViewModel> {

        MainActivityViewModel.Factory(this)

    }

    private lateinit var binding: ActivityMainBinding
    private var checkIdSong : Long = 0L
    private val songs = arrayListOf<Song>()
    private val REQUEST_CODE_READ_STORAGE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listenViewModel()

        with(binding){

            recyclerView.apply {

                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(false)
                adapter = ListMusicView(songs, this@MainActivity)
            }

            tt2.text = "....."
            tt3.text = "....."
        }
    }

    private fun listenViewModel() {

        lifecycleScope.launch {

            viewModel.allSongs.collectLatest { allSongs ->

                allSongs.forEach{

                    songs.add(it)
                }
            }
        }
    }

    override fun playNewSong(item: Song, mMediaPlayer: MediaPlayer) {

        if (item.idSong != checkIdSong){

            checkIdSong = item.idSong

            mMediaPlayer.reset()

            val trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, item.idSong )

            try {

                mMediaPlayer.setDataSource(this@MainActivity, trackUri)
                mMediaPlayer.prepareAsync()

            } catch (e: Exception) {

                Log.e("MUSIC SERVICE", "Error starting data source", e)
            }
        }

        with(binding){

            chadia.visibility = View.VISIBLE
            tt2.text = item.nameSong
            tt3.text = item.singerSong
        }
    }

    private suspend fun checkAudioPermission() {
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                    REQUEST_CODE_READ_STORAGE
                )
            } else {

                MusicRepository(this).getAllSongs()
            }
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_READ_STORAGE
                )
            } else {

                MusicRepository(this).getAllSongs()
            }
        }
    }
}