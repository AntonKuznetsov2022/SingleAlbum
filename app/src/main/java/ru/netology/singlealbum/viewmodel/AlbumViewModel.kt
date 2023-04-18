package ru.netology.singlealbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.singlealbum.model.FeedModel
import ru.netology.singlealbum.repository.AlbumRepositoryImpl
import kotlin.concurrent.thread

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AlbumRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    init {
        loadAlbum()
    }

    fun loadAlbum() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val album = repository.getAll().let { album ->
                    album.copy(tracks = album.tracks.map { it.copy(album = album.title) })
                }
                FeedModel(album = album)
            } catch (e: Exception) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }
}