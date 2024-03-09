package com.example.mealplan.data.repository

import androidx.lifecycle.LiveData
import com.crazylegend.kotlinextensions.livedata.asLiveData
import com.example.mealplan.data.db.MealDao
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.network.ApiServices
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetailRepository @Inject constructor(private val api: ApiServices, private val dao: MealDao) {
    suspend fun getMealInfo(id: Int) = api.getMealInformation(id)
    suspend fun getMealVideo(title: String) = api.getMealVideo(title)
    suspend fun getMealNutrition(id: Int) = api.getMealNutrition(id)
    suspend fun getMealNutritionChart(id: Int) = api.getMealNutritionChart(id)
    suspend fun getSimilar(id: Int) = api.getSimilarRecipes(id)

    //database
    suspend fun insertDailyMeal(entity: DailyMealEntity) = dao.insertDailyMeal(entity)
    suspend fun deleteDailyMeal(entity: DailyMealEntity) = dao.deleteDailyMeal(entity)
    fun getDailyMealsByTime(time: String) = dao.getDailyMealsByTime(time)
    fun existMeal(id: Int) = dao.existMeal(id)
    fun getSingleMealById(id: Int): DailyMealEntity? = dao.getSingleMealById(id)
    fun getAllMeals() = dao.getAllMeals()

}