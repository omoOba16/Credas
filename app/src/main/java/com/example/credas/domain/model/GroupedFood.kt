package com.example.credas.domain.model

sealed class GroupedFood {
    data class Category(val name: String) : GroupedFood()
    data class Foods(val food: Food) : GroupedFood()
}