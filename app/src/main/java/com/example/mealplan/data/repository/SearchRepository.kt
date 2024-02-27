package com.example.mealplan.data.repository

import com.example.mealplan.data.network.ApiServices
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getSearchMeals(queries:Map<String,String>) = api.getMeals(queries)
}