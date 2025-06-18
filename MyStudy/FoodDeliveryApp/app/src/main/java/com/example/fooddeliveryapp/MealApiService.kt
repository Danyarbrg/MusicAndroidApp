package com.example.fooddeliveryapp.network

import android.content.Context
import com.example.fooddeliveryapp.data.Meal
import com.example.fooddeliveryapp.data.MealResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MealApiService private constructor(context: Context) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val mealApi: MealApi = retrofit.create(MealApi::interface)

    companion object {
        @Volatile
        private var instance: MealApiService? = null

        fun getInstance(context: Context): MealApiService {
            return instance ?: synchronized(this) {
                instance ?: MealApiService(context).also { instance = it }
            }
        }
    }

    suspend fun getMeals(category: String): Result<List<Meal>> {
        return try {
            val response = mealApi.getMeals(category)
            Result.success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}