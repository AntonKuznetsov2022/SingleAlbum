package ru.netology.singlealbum.dto

data class Album(
    val id: Long,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<Tracks>
)

data class Tracks(
    val id: Int,
    val file: String,
    val album: String? = null,
    var playTracks: Boolean = false,
)