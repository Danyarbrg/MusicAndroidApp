package com.example.fooddeliveryapp.network

import com.example.fooddeliveryapp.data.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("filter.php")
    suspend fun getMeals(@Query("c") category: String): MealResponse
}