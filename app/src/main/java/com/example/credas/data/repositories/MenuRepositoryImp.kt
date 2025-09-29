package com.example.credas.data.repositories

import com.example.credas.data.mapper.toFoodDetails
import com.example.credas.data.mapper.toMenu
import com.example.credas.data.model.NetworkState
import com.example.credas.data.source.MenuRemoteDataSource
import com.example.credas.domain.model.Food
import com.example.credas.domain.model.FoodDetails
import com.example.credas.domain.repositories.MenuRepository
import com.example.credas.utils.safeCall
import javax.inject.Inject

class MenuRepositoryImp @Inject constructor(
    private val menuRemoteDataSource: MenuRemoteDataSource
): MenuRepository {

    override suspend fun getMenu(): NetworkState<List<Food>?> {

        return safeCall(
            apiCall = { menuRemoteDataSource.getMenu() },
            mapper = { menuDto -> menuDto?.toMenu() ?: emptyList() }
        )
    }

    override suspend fun getFood(id: Int): NetworkState<FoodDetails?> {
        return safeCall(
            apiCall = { menuRemoteDataSource.getFood(id) },
            mapper = { foodDto -> foodDto?.toFoodDetails() }
        )
    }

}