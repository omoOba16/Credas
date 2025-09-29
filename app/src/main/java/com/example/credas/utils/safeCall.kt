package com.example.credas.utils

import com.example.credas.data.model.NetworkState
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <DTO, DOMAIN> safeCall(
    crossinline apiCall: suspend () -> Response<DTO>?,
    crossinline mapper: (DTO?) -> DOMAIN
): NetworkState<DOMAIN?> {
    return try {
        val response = apiCall()
        if (response?.isSuccessful == true){
            NetworkState.Success(mapper(response.body()))
        } else {
            NetworkState.Error("API Error: ${response?.message()} (Code: ${response?.code()})")
        }
    } catch (e: Exception){
        when (e) {
            is SocketTimeoutException -> NetworkState.Error("Timeout Error: Please check your connection.")
            is UnknownHostException -> NetworkState.Error("Network Error: Check your internet connection.")
            is IOException -> NetworkState.Error("Network Error: An I/O error occurred.")
            else -> NetworkState.Error(e.message ?: "An unknown error occurred.")
        }
    }
}