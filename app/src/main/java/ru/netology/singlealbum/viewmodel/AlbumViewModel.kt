package ru.netology.singlealbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.dto.Song
import ru.netology.singlealbum.model.FeedModel
import ru.netology.singlealbum.repository.AlbumRepository
import ru.netology.singlealbum.repository.AlbumRepositoryImpl

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AlbumRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    init {
        loadAlbum()
    }

    fun loadAlbum() {
        _data.value = FeedModel(loading = true)
        repository.get(object : AlbumRepository.Callback {
            override fun onSuccess(album: Album) {
                _data.postValue(FeedModel(album = album.copy(tracks = album.tracks?.map {
                    it.copy(album = album.title)
                })))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
}