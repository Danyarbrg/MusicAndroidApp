package com.example.foodapp.network

import com.example.foodapp.model.MealDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealsResponse

    @GET("lookup.php")
    suspend fun getMealDetail(@Query("i") id: String): MealDetailResponse
}

data class MealsResponse(val meals: List<MealSummary>?)
data class MealSummary(val idMeal: String, val strMeal: String, val strMealThumb: String?)
data class MealDetailResponse(val meals: List<MealDetail>?)