package com.example.credas.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodDto(
    @SerialName("category")
    val category: String,
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Double
)