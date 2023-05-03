package ru.netology.singlealbum.repository

import ru.netology.singlealbum.dto.Album

interface AlbumRepository {
    fun get(callback: Callback)

    interface Callback {
        fun onSuccess(album: Album)
        fun onError(e: Exception)
    }
}