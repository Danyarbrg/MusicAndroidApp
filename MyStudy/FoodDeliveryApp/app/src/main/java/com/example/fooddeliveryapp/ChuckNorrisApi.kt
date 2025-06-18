package com.example.foodapp

import retrofit2.http.GET

data class ChuckNorrisJoke(
    val value: String
)

interface ChuckNorrisApi {
    @GET("jokes/random")
    suspend fun getRandomJoke(): ChuckNorrisJoke
}