package com.example.credas.domain.model

data class FoodDetails (
    val category: String,
    val id: Int,
    val name: String,
    val prettyPrice: String,
    val description: String,
    val image: String,
    val price: Double
)