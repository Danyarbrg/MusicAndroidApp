package com.example.fooddeliveryapp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.CartItem
import com.example.fooddeliveryapp.service.CartService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cartService: CartService) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var cartItems by remember { mutableStateOf(cartService.getCartItems()) }
    val totalPrice by remember(cartItems) {
        mutableStateOf(cartItems.sumOf { it.meal.price * it.quantity })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.cart)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text(stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(cartItems) { item ->
                CartItemRow(item)
            }
            item {
                Text(
                    text = stringResource(R.string.total_price, totalPrice),
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = {
                        cartService.clearCart()
                        cartItems = emptyList()
                        coroutineScope.launch {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Корзина очищена", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(stringResource(R.string.clear_cart))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Text(
        text = "${item.meal.name} x${item.quantity} - $${item.meal.price * item.quantity}",
        modifier = Modifier.padding(16.dp)
    )
}
