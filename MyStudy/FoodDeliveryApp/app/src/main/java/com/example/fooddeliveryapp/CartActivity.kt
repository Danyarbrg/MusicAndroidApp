package com.example.foodapp

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.model.MealDetail

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val container = findViewById<LinearLayout>(R.id.cart_items_container)
        val clearBtn = findViewById<Button>(R.id.btn_clear_cart)

        val items = CartManager.getItems()
        if (items.isEmpty()) {
            container.addView(TextView(this).apply { text = "Корзина пуста" })
        } else {
            items.forEach { item ->
                val tv = TextView(this)
                tv.text = item.strMeal
                container.addView(tv)
            }
        }

        clearBtn.setOnClickListener {
            CartManager.clearCart()
            container.removeAllViews()
            container.addView(TextView(this).apply { text = "Корзина пуста" })
        }
    }
}