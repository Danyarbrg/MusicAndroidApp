package com.example.mymusicapp.network

import com.example.mymusicapp.data.Track
import com.google.gson.annotations.SerializedName

data class JamendoResponse(
    @SerializedName("results") val tracks: List<Track>
)