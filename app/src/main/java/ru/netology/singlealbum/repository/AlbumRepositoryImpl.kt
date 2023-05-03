package ru.netology.singlealbum.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.dto.Song
import java.io.IOException
import java.util.concurrent.TimeUnit


class AlbumRepositoryImpl : AlbumRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<Album>() {}
    private lateinit var song: Song

    companion object {
        private const val BASE_URL =
            "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"
    }

    override fun get(callback: AlbumRepository.Callback) {
        val request: Request = Request.Builder()
            .url(BASE_URL)
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
}