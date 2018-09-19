package com.example.delivery.model

import com.google.gson.annotations.SerializedName

data class DeliveryModel(
        @SerializedName("id") val id: Int, // 20
        @SerializedName("description") val description: String, // Deliver toys to Luqman
        @SerializedName("imageUrl") val imageUrl: String, // https://s3-ap-southeast-1.amazonaws.com/lalamove-mock-api/images/pet-5.jpeg
        @SerializedName("location") val location: Location
)