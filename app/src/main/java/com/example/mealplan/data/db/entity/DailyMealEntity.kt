package com.example.mealplan.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mealplan.utils.Constants

@Entity(Constants.DAILY_MEAL_TABLE)
data class DailyMealEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int?=null,
    var mealTime: String?=null,
    var title: String?=null,
    var image: String?=null,
    var calories: Int?=null
)
