package com.example.fooddeliveryapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fooddeliveryapp.service.CartService
import com.example.fooddeliveryapp.ui.AppNavigation
import com.example.fooddeliveryapp.ui.FoodDeliveryAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var cartService: CartService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate: Запуск активности")
        cartService = CartService()
        setContent {
            FoodDeliveryAppTheme {
                AppNavigation(cartService)
            }
        }
    }
}