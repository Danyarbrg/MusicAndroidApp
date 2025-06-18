package com.example.fooddeliveryapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.fooddeliveryapp.data.CartItem
import com.example.fooddeliveryapp.data.Meal

class CartService : Service() {
    private val cartItems = mutableListOf<CartItem>()

    override fun onBind(intent: Intent?): IBinder? = null

    fun addToCart(meal: Meal) {
        val existingItem = cartItems.find { it.meal.id == meal.id }
        if (existingItem != null) {
            cartItems.remove(existingItem)
            cartItems.add(CartItem(meal, existingItem.quantity + 1))
        } else {
            cartItems.add(CartItem(meal, 1))
        }
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()

    fun clearCart() {
        cartItems.clear()
    }
}