package com.example.mealplan.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.db.entity.WeeklyMealEntity

@Database(
    entities = [DailyMealEntity::class, WeeklyMealEntity::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(MealTypeConverters::class)
abstract class MealAppDataBase : RoomDatabase() {
    abstract fun dao(): MealDao
}