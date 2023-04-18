package ru.netology.singlealbum.model

import ru.netology.singlealbum.dto.Album

data class FeedModel(
    val album: Album? = null,
    val loading: Boolean = false,
    val error: Boolean = false,
)