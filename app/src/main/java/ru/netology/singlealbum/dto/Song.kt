package ru.netology.singlealbum.dto

data class Song(
    val id: Int = 0,
    val file: String,
    val album: String? = null,
    var playTracks: Boolean = false,
)