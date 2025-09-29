package com.example.credas.domain.useCases

import android.util.Log
import com.example.credas.data.model.NetworkState
import com.example.credas.domain.model.FoodDetails
import com.example.credas.domain.repositories.MenuRepository
import javax.inject.Inject

class GetFoodDetailsUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {
    private val TAG = "GetFoodDetailsUseCase"

    suspend operator fun invoke(foodId: Int): NetworkState<FoodDetails?> {
        val response = menuRepository.getFood(foodId)
        return when(response){
            is NetworkState.Success -> {
                Log.d(TAG, "Success: ${response.data}")
                NetworkState.Success(response.data)
            }
            is NetworkState.Error -> {
                Log.d(TAG, "Error: ${response.message}")
                val error = response.message
                NetworkState.Error(error)
            }
        }
    }

}