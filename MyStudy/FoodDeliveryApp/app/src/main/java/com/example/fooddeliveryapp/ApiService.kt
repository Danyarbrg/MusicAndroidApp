package com.example.foodapp

import retrofit2.http.GET

data class SampleData(
    val id: Int,
    val title: String
)

interface ApiService {
    @GET("posts/1")
    suspend fun getSampleData(): SampleData
}