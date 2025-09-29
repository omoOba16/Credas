package com.example.credas.data.mapper

import com.example.credas.data.model.FoodDto
import com.example.credas.data.model.MenuDtoItem
import com.example.credas.domain.model.Food
import com.example.credas.domain.model.FoodDetails
import java.util.Currency
import java.util.Locale

fun List<MenuDtoItem?>.toMenu(): List<Food>? {
    return this.map { it ->
        Food(
            category = it?.category ?: "",
            id = it?.id ?: 0,
            name = it?.name ?: "",
            prettyPrice = "${Currency.getInstance(Locale.UK).symbol}${(it?.price ?: 0.0)}",
            price = it?.price ?: 0.0
        )
    }
}

fun FoodDto?.toFoodDetails(): FoodDetails? {
    return FoodDetails(
        category = this?.category ?: "",
        description = this?.description ?: "",
        id = this?.id ?: 0,
        image = this?.image ?: "",
        name = this?.name ?: "",
        prettyPrice = "${Currency.getInstance(Locale.UK).symbol}${(this?.price ?: 0.0)}",
        price = this?.price ?: 0.0
    )
}