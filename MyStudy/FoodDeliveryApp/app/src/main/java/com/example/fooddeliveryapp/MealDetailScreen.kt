package com.example.fooddeliveryapp.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.Meal
import com.example.fooddeliveryapp.network.MealApiService
import com.example.fooddeliveryapp.service.CartService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MealDetailScreen(navController: NavController, category: String, cartService: CartService?) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var meals by remember { mutableStateOf<List<Meal>>(emptyList()) }
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(category) {
        isLoading = true
        MealApiService.getInstance(context).getMeals(category).fold(
            onSuccess = { fetchedMeals ->
                meals = fetchedMeals
                isLoading = false
            },
            onFailure = { e ->
                error = e.message
                isLoading = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Категория: $category") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text(stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(stringResource(R.string.loading), modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                error != null -> {
                    Text(stringResource(R.string.error, error ?: "Неизвестная ошибка"))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(meals) { meal ->
                            Text(
                                text = "${meal.name} - $${meal.price}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedMeal = meal }
                                    .padding(16.dp)
                            )
                            Divider()
                        }
                    }
                    selectedMeal?.let { meal ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                GlideImage(
                                    model = meal.imageUrl,
                                    contentDescription = stringResource(R.string.meal_image),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = meal.name ?: "Без названия",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = "Категория: ${meal.category ?: "Неизвестно"}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = "Цена: $${meal.price}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = meal.description?.take(100) ?: "Нет описания",
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Button(
                                    onClick = {
                                        cartService?.addToCart(meal)
                                        coroutineScope.launch {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, "${meal.name} добавлено", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Text(stringResource(R.string.add_to_cart))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}