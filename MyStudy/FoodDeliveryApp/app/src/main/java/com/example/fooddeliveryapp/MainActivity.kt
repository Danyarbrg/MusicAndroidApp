package com.example.foodapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foodapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notificationPermissionRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Создание канала уведомлений
        createNotificationChannel()

        // Запрос разрешения на уведомления для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    notificationPermissionRequestCode
                )
            } else {
                // Если разрешение уже есть, показываем уведомление
                fetchAndShowJokeNotification()
            }
        } else {
            // Для версий ниже Android 13 показываем уведомление сразу
            fetchAndShowJokeNotification()
        }

        // Обработка нажатий на категории
        binding.btnBeef.setOnClickListener {
            openCategory("Beef")
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Joke Notifications"
            val descriptionText = "Channel for random joke notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("JOKE_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun fetchAndShowJokeNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://official-joke-api.appspot.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val jokeApi = retrofit.create(JokeApi::class.java)
                val joke = jokeApi.getJoke()

                withContext(Dispatchers.Main) {
                    showNotification(joke)
                }
            } catch (e: Exception) {
                // Обработка ошибки, если API недоступен
            }
        }
    }

    private fun showNotification(joke: Joke) {
        val builder = NotificationCompat.Builder(this, "JOKE_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Замените на ваш ресурс иконки
            .setContentTitle(joke.setup)
            .setContentText(joke.punchline)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notify(1, builder.build())
                }
            } else {
                notify(1, builder.build())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchAndShowJokeNotification()
        }
    }

    private fun openCategory(category: String) {
        val intent = Intent(this, FoodTypeActivity::class.java)
        intent.putExtra("food_type", category)
        startActivity(intent)
    }
}