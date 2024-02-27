package com.example.mealplan.utils.network

sealed class NetworkStatus<T>(val success: T? = null, val error: String? = null) {

    class Loading<T> : NetworkStatus<T>()
    class Success<T>(data:T) : NetworkStatus<T>(data)
    class Error<T>(message:String) : NetworkStatus<T>(error = message)
}