package com.example.credas.data.source

import com.example.credas.data.network.ApiService
import javax.inject.Inject

class MenuRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMenu() = apiService.getMenu()
    suspend fun getFood(id: Int) = apiService.getFood(id)
}