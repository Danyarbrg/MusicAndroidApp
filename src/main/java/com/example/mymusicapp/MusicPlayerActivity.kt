package com.example.mymusicapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MusicPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        // Привязка элементов
        val buttonPrevious = findViewById<Button>(R.id.button_previous)
        val buttonPlayPause = findViewById<Button>(R.id.button_play_pause)
        val buttonNext = findViewById<Button>(R.id.button_next)
        val buttonAbout = findViewById<Button>(R.id.button_about)
        val seekBar = findViewById<SeekBar>(R.id.seek_bar)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val genreText = findViewById<TextView>(R.id.genre_text)
        val buttonBack = findViewById<Button>(R.id.button_back)

        // Извлечение жанра из Intent
        val genre = intent.getStringExtra("GENRE_NAME") ?: "Неизвестный жанр"
        if (genreText != null) {
            genreText.text = "Жанр: $genre"
        } else {
            Toast.makeText(this, "Ошибка: Элемент жанра не найден", Toast.LENGTH_SHORT).show()
        }

        // Действия для кнопок
        buttonPrevious.setOnClickListener { Toast.makeText(this, "Предыдущий трек", Toast.LENGTH_SHORT).show() }
        buttonPlayPause.setOnClickListener { Toast.makeText(this, "Воспроизведение", Toast.LENGTH_SHORT).show() }
        buttonNext.setOnClickListener { Toast.makeText(this, "Следующий трек", Toast.LENGTH_SHORT).show() }
        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // Переход на карточку исполнителя при нажатии на имя
        artistName.setOnClickListener {
            val intent = Intent(this, ArtistCardActivity::class.java)
            startActivity(intent)
        }

        // Действие для кнопки "Назад"
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Закрываем текущую активность
        }

        // Действие для SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@MusicPlayerActivity, "Прогресс изменен", Toast.LENGTH_SHORT).show()
            }
        })

        // Действие для RatingBar
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this, "Оценка: $rating", Toast.LENGTH_SHORT).show()
        }
    }
}