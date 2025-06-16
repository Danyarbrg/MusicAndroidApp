package com.example.mymusicapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // экран с жанрами

        val popGenre: ImageView = findViewById(R.id.genre1_image)
        val rockGenre: ImageView = findViewById(R.id.genre2_image)
        val jazzGenre: ImageView = findViewById(R.id.genre3_image)
        val classicGenre: ImageView = findViewById(R.id.genre4_image)

        popGenre.setOnClickListener {
            openMusicPlayer("Фонк")
        }

        rockGenre.setOnClickListener {
            openMusicPlayer("Электроника")
        }

        jazzGenre.setOnClickListener {
            openMusicPlayer("Джаз")
        }

        classicGenre.setOnClickListener {
            openMusicPlayer("Опера")
        }
    }

    private fun openMusicPlayer(genre: String) {
        val intent = Intent(this, MusicPlayerActivity::class.java)
        intent.putExtra("GENRE_NAME", genre)
        startActivity(intent)
    }
}
