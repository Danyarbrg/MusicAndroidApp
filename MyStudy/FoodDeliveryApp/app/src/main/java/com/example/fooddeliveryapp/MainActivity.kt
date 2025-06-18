package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обработка нажатий на категории
        binding.btnBeef.setOnClickListener {
            openCategory("Beef") // Используйте "Beef" с большой буквы для соответствия API
        }
        binding.btnPasta.setOnClickListener {
            openCategory("Pasta")
        }
        binding.btnSeafood.setOnClickListener {
            openCategory("Seafood")
        }

        // Кнопка корзины
        binding.btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Кнопка "О приложении"
        binding.btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openCategory(category: String) {
        val intent = Intent(this, FoodTypeActivity::class.java)
        intent.putExtra("food_type", category) // Изменено с "CATEGORY" на "food_type"
        startActivity(intent)
    }
}