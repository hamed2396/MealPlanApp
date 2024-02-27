package com.example.mealplan.utils.network

import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.example.mealplan.data.models.ErrorResponse
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.Constants.AUTHORIZATION_ERROR
import com.example.mealplan.utils.Constants.ERROR_CONNECTION
import com.example.mealplan.utils.Constants.SERVER_ERROR
import com.example.mealplan.utils.network.NetworkStatus.Error
import com.example.mealplan.utils.network.NetworkStatus.Success
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class NetworkResponse @Inject constructor() {

    fun <T> handleResponse(response: Response<T>): NetworkStatus<T> {
        return when {
            response.isSuccessful -> Success(response.body()!!)
            response.code() == 401 -> Error(AUTHORIZATION_ERROR)
            response.code() == 422 -> {
                var errorMessage = ""
                var message = ""

                response.errorBody()?.let {

                    val errorResponse = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        ErrorResponse::class.java
                    )
                    val errors = errorResponse.errors
                    errors?.forEach { (_, errorsList) ->
                        errorMessage = errorsList.joinToString()
                    }
                    message = errorResponse.message!!

                }

                Error(if (errorMessage.isNotNullOrEmpty()) errorMessage else message)
            }

            response.code() == 500 -> Error(SERVER_ERROR)

            else -> {
                val message = if (response.message().isNullOrEmpty().not()) response.message() else ERROR_CONNECTION
                Error(message)
            }
        }
    }
}