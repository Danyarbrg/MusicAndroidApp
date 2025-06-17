package com.example.mymusicapp.data

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("name") val name: String?,
    @SerializedName("artist_name") val artist_name: String?,
    @SerializedName("audio") val audio: String?
)