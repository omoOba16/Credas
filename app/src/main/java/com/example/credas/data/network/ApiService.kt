package com.example.credas.data.network

import com.example.credas.data.model.FoodDto
import com.example.credas.data.model.MenuDtoItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("menu")
    suspend fun getMenu(): Response<List<MenuDtoItem?>>

    @GET("menu/{id}")
    suspend fun getFood(@Path("id") id: Int): Response<FoodDto?>
}