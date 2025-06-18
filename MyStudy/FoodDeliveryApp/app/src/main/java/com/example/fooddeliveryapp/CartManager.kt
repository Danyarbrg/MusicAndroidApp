package com.example.foodapp

import com.example.foodapp.model.MealDetail

object CartManager {
    private val cartItems = mutableListOf<MealDetail>()

    fun addItem(item: MealDetail) {
        cartItems.add(item)
    }

    fun getItems(): List<MealDetail> = cartItems

    fun clearCart() {
        cartItems.clear()
    }
}