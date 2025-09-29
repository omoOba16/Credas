package com.example.credas.data.model

sealed class NetworkState<out T> {
    data class Success<T>(val data: T) : NetworkState<T>()
    data class Error<T>(val message: String) : NetworkState<T>()
}