package com.example.mymusicapp.network

import android.content.Context
import android.util.Log
import com.example.mymusicapp.data.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoService {
    @GET("v3.0/tracks/")
    fun getTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 50,
        @Query("tags") genre: String
    ): Call<JamendoResponse>
}

class JamendoApi private constructor(context: Context) {
    private val retrofit: Retrofit
    private val service: JamendoService
    private val TAG = "JamendoApi"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.jamendo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(JamendoService::class.java)
    }

    fun getTracks(
        genre: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "getTracks: Запрос треков для жанра: $genre")
        val call = service.getTracks(
            clientId = CLIENT_ID,
            genre = genre
        )
        call.enqueue(object : Callback<JamendoResponse> {
            override fun onResponse(call: Call<JamendoResponse>, response: Response<JamendoResponse>) {
                Log.d(TAG, "onResponse: Код ответа: ${response.code()}")
                if (response.isSuccessful) {
                    val tracks = response.body()?.tracks ?: emptyList()
                    Log.d(TAG, "onResponse: Получено ${tracks.size} треков: ${tracks.take(3)}")
                    onSuccess(tracks)
                } else {
                    val error = "Ошибка API: ${response.code()} ${response.message()}"
                    Log.e(TAG, "onResponse: $error")
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "onResponse: Тело ошибки: $errorBody")
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse: Не удалось прочитать тело ошибки: ${e.message}")
                    }
                    onError(error)
                }
            }

            override fun onFailure(call: Call<JamendoResponse>, t: Throwable) {
                val error = "Сетевая ошибка: ${t.message}"
                Log.e(TAG, "onFailure: $error", t)
                onError(error)
            }
        })
    }

    companion object {
        private const val CLIENT_ID = "a4f7e71a"
        @Volatile
        private var instance: JamendoApi? = null

        fun getInstance(context: Context): JamendoApi {
            return instance ?: synchronized(this) {
                instance ?: JamendoApi(context.applicationContext).also { instance = it }
            }
        }
    }
}