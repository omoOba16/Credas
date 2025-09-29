package com.example.credas.domain.repositories

import com.example.credas.data.model.NetworkState
import com.example.credas.domain.model.Food
import com.example.credas.domain.model.FoodDetails

interface MenuRepository {

    suspend fun getMenu(): NetworkState<List<Food>?>
    suspend fun getFood(id: Int): NetworkState<FoodDetails?>

}