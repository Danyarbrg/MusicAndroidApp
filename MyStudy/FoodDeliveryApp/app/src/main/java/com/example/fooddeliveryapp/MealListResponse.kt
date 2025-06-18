package com.example.foodapp.model

data class MealListResponse(
    val meals: List<MealSummary>?
)

data class MealSummary(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)