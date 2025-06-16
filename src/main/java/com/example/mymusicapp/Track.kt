package com.example.mymusicapp.data

data class Track(
    val id: Int,
    val name: String,
    val artist_name: String,
    val audio: String,
    val image: String
)

data class TracksResponse(
    val results: List<Track>
)