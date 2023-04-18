package ru.netology.singlealbum

import TracksAdapter
import OnInteractionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Tracks
import ru.netology.singlealbum.viewmodel.AlbumViewModel

class MainActivity : AppCompatActivity() {

    private val observe = MediaLifecycleObserve()
    private val url =
        "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/"
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.addObserver(observe)

        val adapter = TracksAdapter(object : OnInteractionListener {
            var lastSong: Tracks = Tracks(-1,"","")

            override fun play(tracks: Tracks) {

                if (!tracks.playTracks) {
                    observe.mediaPlayer?.stop()
                    observe.mediaPlayer?.reset()
                    tracks.playTracks = false
                    observe.apply {
                        mediaPlayer?.setDataSource(url + tracks.file)
                        tracks.playTracks = true
                    }.play()

                    observe.mediaPlayer?.setOnCompletionListener {
                        val adapter = binding.musicList.adapter as TracksAdapter
                        val nextId = adapter.getId(tracks.id)
                        val nextSong = adapter.nextTrack(nextId)
                        play(nextSong)
                    }
                    
                } else {
                    if (observe.mediaPlayer?.isPlaying == true) {
                        observe.mediaPlayer?.pause()
                    } else {
                        observe.mediaPlayer?.start()
                        tracks.playTracks = true
                    }
                }
            }
        })

        binding.musicList.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it.album?.tracks)
            binding.apply {
                albumName.text = it.album?.title
                artistName.text = it.album?.artist
                published.text = it.album?.published
                genre.text = it.album?.genre
            }
            if (it.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) {
                        viewModel.loadAlbum()
                    }
                    .show()
            }
        }

    }
}