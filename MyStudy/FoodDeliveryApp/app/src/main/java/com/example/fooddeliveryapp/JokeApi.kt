package com.example.foodapp

import retrofit2.http.GET

interface JokeApi {
    @GET("random_joke")
    suspend fun getJoke(): Joke
}

data class Joke(val setup: String, val punchline: String)