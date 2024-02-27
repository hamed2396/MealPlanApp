package com.example.mealplan.data.repository

import com.example.mealplan.data.models.signup.BodySignUp
import com.example.mealplan.data.network.ApiServices
import javax.inject.Inject

class MealRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getRandomMeals(queries:Map<String,String>) = api.getMeals(queries)
}