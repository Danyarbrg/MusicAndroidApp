package com.example.mymusicapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ArtistCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_card)

        // Настройка кнопки "Назад"
        val buttonBack = findViewById<Button>(R.id.button_back)
        buttonBack.setOnClickListener {
            finish() // Возвращаемся к предыдущему экрану
        }
    }
}