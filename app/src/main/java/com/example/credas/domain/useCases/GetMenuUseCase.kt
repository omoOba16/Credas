package com.example.credas.domain.useCases

import android.util.Log
import com.example.credas.data.model.NetworkState
import com.example.credas.domain.model.Food
import com.example.credas.domain.model.GroupedFood
import com.example.credas.domain.repositories.MenuRepository
import java.util.Locale
import javax.inject.Inject

class GetMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {
    private val TAG = "GetMenuUseCase"

    suspend operator fun invoke(): NetworkState<List<GroupedFood>> {
        val response = menuRepository.getMenu()
        return when(response){
            is NetworkState.Success -> {
                Log.d(TAG, "Success: ${response.data}")
                val result = groupedFoods(response.data)
                NetworkState.Success(result)
            }
            is NetworkState.Error -> {
                Log.d(TAG, "Error: ${response.message}")
                val error = response.message
                NetworkState.Error(error)
            }
        }
    }

    fun groupedFoods(data: List<Food>?): List<GroupedFood> {
        val foods = mutableListOf<GroupedFood>()
        data?.groupBy { it.category }?.forEach {
            foods.add(GroupedFood.Category(it.key.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }))
            foods.addAll(it.value.map { food -> GroupedFood.Foods(food) })
        }
        return foods
    }
}