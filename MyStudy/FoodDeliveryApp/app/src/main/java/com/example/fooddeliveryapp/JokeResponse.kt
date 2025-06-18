package com.example.foodapp

import com.google.gson.annotations.SerializedName

data class JokeResponse(
    @SerializedName("idMeal") val id: String?,
    @SerializedName("strMeal") val name: String?,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strMealThumb") val imageUrl: String?,
    @SerializedName("strInstructions") val description: String?,
    val price: Double = 10.0 // Заглушка для цены
)