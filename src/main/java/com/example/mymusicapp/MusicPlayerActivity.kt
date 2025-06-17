package com.example.mymusicapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.mymusicapp.network.JamendoApi

class MusicPlayerActivity : AppCompatActivity() {

    private var player: Player? = null
    private var currentTrackIndex = -1
    private val tracks = mutableListOf<com.example.mymusicapp.data.Track>()
    private var tracksList: ListView? = null
    private var playPauseButton: Button? = null
    private var isPlaying = false
    private val TAG = "MusicPlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Запуск активности")

        val genre = intent.getStringExtra("GENRE_NAME")?.toLowerCase() ?: "unknown"
        Log.d(TAG, "onCreate: Жанр = $genre")

        if (genre == "опера" || genre == "opera") {
            try {
                setContentView(R.layout.activity_music_player)
                Log.d(TAG, "onCreate: Установлен layout activity_music_player")

                val songTitle = findViewById<TextView>(R.id.song_title) ?: throw IllegalStateException("Не найден song_title")
                val artistName = findViewById<TextView>(R.id.artist_name) ?: throw IllegalStateException("Не найден artist_name")
                val genreText = findViewById<TextView>(R.id.genre_text) ?: throw IllegalStateException("Не найден genre_text")
                val seekBar = findViewById<SeekBar>(R.id.seek_bar) ?: throw IllegalStateException("Не найден seek_bar")
                val buttonPrevious = findViewById<Button>(R.id.button_previous) ?: throw IllegalStateException("Не найден button_previous")
                val buttonPlayPause = findViewById<Button>(R.id.button_play_pause) ?: throw IllegalStateException("Не найден button_play_pause")
                val buttonNext = findViewById<Button>(R.id.button_next) ?: throw IllegalStateException("Не найден button_next")
                val buttonAbout = findViewById<Button>(R.id.button_about) ?: throw IllegalStateException("Не найден button_about")
                val buttonBack = findViewById<Button>(R.id.button_back) ?: throw IllegalStateException("Не найден button_back")
                val ratingBar = findViewById<RatingBar>(R.id.rating_bar) ?: throw IllegalStateException("Не найден rating_bar")
                Log.d(TAG, "onCreate: Все элементы интерфейса для Оперы найдены")

                songTitle.text = "Пиаф"
                artistName.text = "Валерия"
                genreText.text = "Жанр: $genre"

                seekBar.max = 100
                seekBar.progress = 0
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        Toast.makeText(this@MusicPlayerActivity, "Прогресс изменен", Toast.LENGTH_SHORT).show()
                    }
                })

                buttonPlayPause.setOnClickListener {
                    buttonPlayPause.text = if (buttonPlayPause.text == "Играть") "Пауза" else "Играть"
                    Toast.makeText(this@MusicPlayerActivity, "Воспроизведение: ${buttonPlayPause.text}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "buttonPlayPause: Нажата кнопка Play/Pause, текст = ${buttonPlayPause.text}")
                }

                buttonBack.setOnClickListener {
                    Log.d(TAG, "buttonBack: Нажата кнопка Назад")
                    finish()
                    Toast.makeText(this@MusicPlayerActivity, "Возврат на главный экран", Toast.LENGTH_SHORT).show()
                }

                buttonPrevious.setOnClickListener {
                    Toast.makeText(this@MusicPlayerActivity, "Предыдущий трек", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "buttonPrevious: Нажата кнопка Предыдущий трек")
                }

                buttonNext.setOnClickListener {
                    Toast.makeText(this@MusicPlayerActivity, "Следующий трек", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "buttonNext: Нажата кнопка Следующий трек")
                }

                buttonAbout.setOnClickListener {
                    Toast.makeText(this@MusicPlayerActivity, "Открыть экран 'О приложении'", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "buttonAbout: Нажата кнопка О приложении")
                }

                ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                    Toast.makeText(this@MusicPlayerActivity, "Оценка: $rating", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "ratingBar: Установлена оценка $rating")
                }
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: Ошибка в ветке Опера: ${e.message}", e)
                Toast.makeText(this, "Ошибка в интерфейсе Оперы: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        } else {
            try {
                setContentView(R.layout.activity_music_player_list)
                Log.d(TAG, "onCreate: Установлен layout activity_music_player_list")

                val genreText = findViewById<TextView>(R.id.genre_text) ?: throw IllegalStateException("Не найден genre_text")
                tracksList = findViewById<ListView>(R.id.tracks_list) ?: throw IllegalStateException("Не найден tracks_list")
                playPauseButton = findViewById<Button>(R.id.button_play_pause) ?: throw IllegalStateException("Не найден button_play_pause")
                val playerView = findViewById<PlayerView>(R.id.player_view)
                Log.d(TAG, "onCreate: Все элементы интерфейса найдены, playerView = ${playerView != null}")

                genreText.text = "Жанр: $genre"

                player = ExoPlayer.Builder(this).build()
                playerView?.player = player
                Log.d(TAG, "onCreate: ExoPlayer инициализирован и привязан к PlayerView")

                // Установка слушателя для кнопки Play/Pause
                playPauseButton?.setOnClickListener {
                    if (currentTrackIndex != -1) {
                        if (isPlaying) {
                            player?.pause()
                            playPauseButton?.text = "Играть"
                            isPlaying = false
                            Log.d(TAG, "buttonPlayPause: Пауза")
                        } else {
                            player?.play()
                            playPauseButton?.text = "Пауза"
                            isPlaying = true
                            Log.d(TAG, "buttonPlayPause: Воспроизведение")
                        }
                        Toast.makeText(this, "Состояние: ${playPauseButton?.text}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Выберите трек", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "buttonPlayPause: Трек не выбран")
                    }
                }

                // Нормализация жанра для API
                val apiGenre = when (genre) {
                    "рок" -> "rock"
                    "джаз" -> "jazz"
                    "поп" -> "pop"
                    "классика" -> "classical"
                    else -> genre
                }
                Log.d(TAG, "onCreate: Используется жанр для API: $apiGenre")

                val api = JamendoApi.getInstance(this)
                api.getTracks(
                    genre = apiGenre,
                    onSuccess = { fetchedTracks ->
                        Log.d(TAG, "getTracks: Получено ${fetchedTracks.size} треков")
                        tracks.clear()
                        tracks.addAll(fetchedTracks.filter {
                            !it.name.isNullOrBlank() && !it.artist_name.isNullOrBlank() && !it.audio.isNullOrBlank()
                        })
                        if (tracks.isEmpty()) {
                            Log.w(TAG, "getTracks: Список треков пуст")
                            Toast.makeText(this, "Треки не найдены", Toast.LENGTH_SHORT).show()
                            playPauseButton?.isEnabled = false
                        } else {
                            Log.d(TAG, "getTracks: Треки загружены: ${tracks.take(3)}")
                            val trackNames = tracks.map {
                                "${it.name?.ifBlank { "Без названия" } ?: "Без названия"} by ${it.artist_name?.ifBlank { "Неизвестный артист" } ?: "Неизвестный артист"}"
                            }
                            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, trackNames)
                            tracksList?.adapter = adapter
                            Log.d(TAG, "getTracks: Адаптер установлен")

                            tracksList?.setOnItemClickListener { _, _, position, _ ->
                                currentTrackIndex = position
                                Log.d(TAG, "tracksList: Выбран трек на позиции $position")
                                playTrack(position)
                                playPauseButton?.isEnabled = true
                                playPauseButton?.text = "Пауза"
                                isPlaying = true
                            }

                            // Автоматическое воспроизведение первого трека
                            if (tracks.isNotEmpty()) {
                                currentTrackIndex = 0
                                playTrack(0)
                                playPauseButton?.isEnabled = true
                                playPauseButton?.text = "Пауза"
                                isPlaying = true
                                Log.d(TAG, "getTracks: Автоматически воспроизведен первый трек")
                            }
                        }
                    },
                    onError = { error ->
                        Log.e(TAG, "getTracks: Ошибка загрузки треков: $error")
                        Toast.makeText(this, "Ошибка загрузки треков: $error", Toast.LENGTH_LONG).show()
                        playPauseButton?.isEnabled = false
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: Ошибка в ветке не-Опера: ${e.message}", e)
                Toast.makeText(this, "Ошибка загрузки жанра: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun playTrack(position: Int) {
        try {
            if (player == null) {
                Log.e(TAG, "playTrack: Плеер не инициализирован")
                Toast.makeText(this, "Ошибка: Плеер не готов", Toast.LENGTH_SHORT).show()
                playPauseButton?.isEnabled = false
                return
            }
            if (position in tracks.indices) {
                val track = tracks[position]
                val audioUrl = track.audio
                Log.d(TAG, "playTrack: Воспроизведение трека ${track.name}, URL: $audioUrl")
                if (audioUrl.isNullOrBlank()) {
                    Log.e(TAG, "playTrack: URL трека пустой")
                    Toast.makeText(this, "Ошибка: URL трека пустой", Toast.LENGTH_SHORT).show()
                    playPauseButton?.isEnabled = false
                    return
                }
                val mediaItem = MediaItem.fromUri(audioUrl)
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.play()
                isPlaying = true
                playPauseButton?.text = "Пауза"
                Toast.makeText(this, "Воспроизведение: ${track.name}", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "playTrack: Неверная позиция трека: $position")
                Toast.makeText(this, "Ошибка: Неверная позиция трека", Toast.LENGTH_SHORT).show()
                playPauseButton?.isEnabled = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "playTrack: Ошибка воспроизведения: ${e.message}", e)
            Toast.makeText(this, "Ошибка воспроизведения: ${e.message}", Toast.LENGTH_LONG).show()
            playPauseButton?.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Освобождение ресурсов")
        try {
            player?.release()
            player = null
            tracksList = null
            playPauseButton = null
            Log.d(TAG, "onDestroy: Ресурсы освобождены")
        } catch (e: Exception) {
            Log.e(TAG, "onDestroy: Ошибка при освобождении ресурсов: ${e.message}", e)
        }
    }
}