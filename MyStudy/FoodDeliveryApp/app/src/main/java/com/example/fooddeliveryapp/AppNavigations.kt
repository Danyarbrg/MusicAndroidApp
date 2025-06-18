package com.example.fooddeliveryapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryapp.service.CartService

@Composable
fun AppNavigation(cartService: CartService) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("cart") { CartScreen(navController, cartService) }
        composable("meal_detail/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            MealDetailScreen(navController, category, cartService)
        }
    }
}