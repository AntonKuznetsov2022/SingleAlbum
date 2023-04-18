package ru.netology.singlealbum.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import ru.netology.singlealbum.dto.Album
import java.util.concurrent.TimeUnit

class AlbumRepositoryImpl {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<Album>() {}
    private val url = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"

    companion object {
        private val jsonType = "application/json".toMediaType()
    }

    fun getAll(): Album {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            } ?: Album(
            id = 0,
            title = "",
            subtitle = "",
            artist = "",
            published = "",
            genre = "",
            tracks = emptyList()
        )
    }

}