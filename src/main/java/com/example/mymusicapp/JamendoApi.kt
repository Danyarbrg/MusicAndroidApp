package com.example.mymusicapp.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mymusicapp.data.Track
import com.google.gson.Gson
import com.example.mymusicapp.data.TracksResponse

class JamendoApi(private val context: Context) {
    private val queue: RequestQueue by lazy { Volley.newRequestQueue(context) }
    private val gson = Gson()
    private val baseUrl = "https://api.jamendo.com/v3.0/tracks/"

    fun getTracks(onSuccess: (List<Track>) -> Unit, onError: (String) -> Unit) {
        val url = "$baseUrl?client_id=a4f7e71a&format=jsonpretty&limit=10"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val tracksResponse = gson.fromJson(response.toString(), TracksResponse::class.java)
                onSuccess(tracksResponse.results)
            },
            { error ->
                onError("Error: ${error.message}")
            }
        )

        queue.add(jsonObjectRequest)
    }

    companion object {
        @Volatile
        private var INSTANCE: JamendoApi? = null

        fun getInstance(context: Context): JamendoApi {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: JamendoApi(context).also { INSTANCE = it }
            }
        }
    }
}