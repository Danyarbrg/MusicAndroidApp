package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodapp.network.MealApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodTypeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var ivFoodImage: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnOpenCart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_type)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ivFoodImage = findViewById(R.id.iv_food_image)
        tvName = findViewById(R.id.tv_food_name)
        tvDescription = findViewById(R.id.tv_description)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        btnOpenCart = findViewById(R.id.btn_open_cart)

        val category = intent.getStringExtra("food_type") ?: "Beef"
        tvName.text = category
        supportActionBar?.title = category

        lifecycleScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(MealApi::class.java)
            val mealsResponse = withContext(Dispatchers.IO) {
                api.getMealsByCategory(category)
            }

            val firstMeal = mealsResponse.meals?.firstOrNull()
            if (firstMeal != null) {
                val detail = withContext(Dispatchers.IO) {
                    api.getMealDetail(firstMeal.idMeal)
                }.meals?.firstOrNull()

                detail?.let { mealDetail ->
                    tvDescription.text = mealDetail.strInstructions
                    Glide.with(this@FoodTypeActivity)
                        .load(mealDetail.strMealThumb)
                        .into(ivFoodImage)

                    btnAddToCart.setOnClickListener {
                        CartManager.addItem(mealDetail) // Use mealDetail instead of it
                        Toast.makeText(
                            this@FoodTypeActivity,
                            "${mealDetail.strMeal} добавлено в корзину",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                tvDescription.text = "Блюда не найдены"
            }
        }

        btnOpenCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}