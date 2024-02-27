package com.example.mealplan.data.repository.detail

import com.example.mealplan.data.network.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getMealInfo(id:Int) = api.getMealInformation(id)
    suspend fun getMealVideo(title:String) = api.getMealVideo(title)
    suspend fun getMealNutrition(id:Int) = api.getMealNutrition(id)
    suspend fun getMealNutritionChart(id:Int) = api.getMealNutritionChart(id)
}