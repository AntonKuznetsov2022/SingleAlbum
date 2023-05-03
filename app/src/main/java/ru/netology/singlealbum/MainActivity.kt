package ru.netology.singlealbum

import TracksAdapter
import OnInteractionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Song
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

        val adapter = TracksAdapter(observe, object : OnInteractionListener {
            var lastSong: Song = Song(-1, "", "", false)

            override fun play(song: Song) {

                if (lastSong.file != song.file) {
                    if (observe.mediaPlayer?.isPlaying == true) {
                        observe.mediaPlayer?.reset()
                        lastSong.playTracks = false
                        binding.musicList.adapter?.notifyItemChanged(lastSong.id - 1)
                    }
                    observe.mediaPlayer?.apply {
                        setDataSource(url + song.file)

                        setOnPreparedListener {
                            it.start()
                            song.playTracks = true
                            lastSong = song
                            binding.musicList.adapter?.notifyItemChanged(song.id - 1)
                        }

                        setOnCompletionListener {
                            val adapter = binding.musicList.adapter as TracksAdapter
                            val nextId = adapter.nextId(song.id)
                            val nextSong = adapter.nextSong(nextId)
                            play(nextSong)
                        }
                        prepareAsync()
                    }
                } else {
                    if (observe.mediaPlayer?.isPlaying == true) {
                        observe.mediaPlayer?.pause()
                    } else {
                        observe.mediaPlayer?.start()
                    }
                    song.playTracks = !song.playTracks
                    binding.musicList.adapter?.notifyItemChanged(song.id - 1)
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