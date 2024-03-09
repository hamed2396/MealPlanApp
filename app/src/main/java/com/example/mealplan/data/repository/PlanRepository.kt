package com.example.mealplan.data.repository

import com.example.mealplan.data.db.MealDao
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.db.entity.WeeklyMealEntity
import com.example.mealplan.data.network.ApiServices
import javax.inject.Inject

class PlanRepository @Inject constructor(private val dao: MealDao, private val api: ApiServices) {
    suspend fun insertDailyMeal(entity: DailyMealEntity) = dao.insertDailyMeal(entity)
    suspend fun deleteDailyMeal(entity: DailyMealEntity) = dao.deleteDailyMeal(entity)
    fun getDailyMealsByTime(time: String) = dao.getDailyMealsByTime(time)
    fun existMeal(id: Int) = dao.existMeal(id)
    fun getAllMeals() = dao.getAllMeals()

    suspend fun getWeeklyMeal() = api.getWeeklyMeal()
    suspend fun insertWeeklyMeal(entity: WeeklyMealEntity) = dao.insertWeeklyMeal(entity)
    fun getAllWeeklyMeals() = dao.getAllWeeklyMeals()
    suspend fun deleteAllWeeklyRecords()=dao.deleteAllWeeklyRecords()

}