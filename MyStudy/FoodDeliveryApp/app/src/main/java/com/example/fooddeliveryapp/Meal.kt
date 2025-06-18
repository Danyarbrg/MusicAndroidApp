package com.example.fooddeliveryapp.data

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idMeal") val id: String?,
    @SerializedName("strMeal") val name: String?,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strMealThumb") val imageUrl: String?,
    @SerializedName("strInstructions") val description: String?,
    val price: Double = 10.0 // Заглушка для цены
)