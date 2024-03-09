package com.example.mealplan.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.db.entity.WeeklyMealEntity
import com.example.mealplan.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyMeal(entity: DailyMealEntity)

    @Delete
    suspend fun deleteDailyMeal(entity: DailyMealEntity)

    @Query("SELECT * FROM ${Constants.DAILY_MEAL_TABLE} WHERE mealTime = :time ")
    fun getDailyMealsByTime(time: String): List<DailyMealEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM ${Constants.DAILY_MEAL_TABLE} WHERE id ==:id)")
    fun existMeal(id: Int): LiveData<Boolean>

    @Query("SELECT * FROM ${Constants.DAILY_MEAL_TABLE} WHERE id = :id")
    fun getSingleMealById(id: Int): DailyMealEntity?

    @Query("SELECT * FROM ${Constants.DAILY_MEAL_TABLE}")
    fun getAllMeals(): Flow<MutableList<DailyMealEntity>>

    @Query("DELETE  FROM ${Constants.DAILY_MEAL_TABLE}")
    fun deleteAllRecords()

    //weekly

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyMeal(entity: WeeklyMealEntity)

    @Delete
    suspend fun deleteWeeklyMeal(entity: WeeklyMealEntity)

    @Query("SELECT * FROM ${Constants.WEEKLY_MEAL_TABLE}")
    fun getAllWeeklyMeals(): WeeklyMealEntity

    @Query("DELETE  FROM ${Constants.WEEKLY_MEAL_TABLE}")
    suspend fun deleteAllWeeklyRecords()

}