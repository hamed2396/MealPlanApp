package com.example.mealplan.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mealplan.data.models.ResponseWeeklyMeals
import com.example.mealplan.utils.Constants

@Entity(Constants.WEEKLY_MEAL_TABLE)
data class WeeklyMealEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var week: ResponseWeeklyMeals? = null


)
