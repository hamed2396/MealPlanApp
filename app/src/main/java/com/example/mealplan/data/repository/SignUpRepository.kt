package com.example.mealplan.data.repository

import com.example.mealplan.data.models.signup.BodySignUp
import com.example.mealplan.data.network.ApiServices
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val api: ApiServices) {
    suspend fun postSignUp(body: BodySignUp) = api.postSignUp(body)
}