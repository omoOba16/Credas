package com.example.credas.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuDtoItem(
    @SerialName("category")
    val category: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Double
)