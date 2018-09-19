package com.example.delivery.model

import com.google.gson.annotations.SerializedName

data class Location(
        @SerializedName("lat") val lat: Double, // 22.319181
        @SerializedName("lng") val lng: Double, // 114.170008
        @SerializedName("address") val address: String // Mong Kok
)