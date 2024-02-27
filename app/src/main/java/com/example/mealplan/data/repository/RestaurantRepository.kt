package com.example.mealplan.data.repository

import com.example.mealplan.data.models.signup.BodySignUp
import com.example.mealplan.data.network.ApiServices
import javax.inject.Inject

class RestaurantRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getRestaurants() = api.getRestaurants()
}