package com.example.mealplan.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.mealplan.data.models.ResponseWeeklyMeals
import com.google.gson.Gson
import javax.inject.Inject

@ProvidedTypeConverter
class MealTypeConverters @Inject constructor(private val gson: Gson) {

    @TypeConverter
    fun recipeToJson(recipes: ResponseWeeklyMeals): String = gson.toJson(recipes)

    @TypeConverter
    fun stringToRecipe(data: String): ResponseWeeklyMeals =
        gson.fromJson(data, ResponseWeeklyMeals::class.java)


}