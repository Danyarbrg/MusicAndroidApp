package com.example.foodapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AboutActivity : AppCompatActivity() {

    private lateinit var infoTextView: TextView
    private lateinit var jokeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "О приложении"

        infoTextView = findViewById(R.id.tv_info)
        jokeTextView = findViewById(R.id.jokeTextView)

        infoTextView.text = "Это учебное приложение о еде"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://official-joke-api.appspot.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val jokeApi = retrofit.create(JokeApi::class.java)
                val joke = jokeApi.getJoke()

                withContext(Dispatchers.Main) {
                    jokeTextView.text = "${joke.setup}\n\n${joke.punchline}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    jokeTextView.text = "Ошибка загрузки шутки"
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}