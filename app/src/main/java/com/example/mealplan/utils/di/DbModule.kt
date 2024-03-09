package com.example.mealplan.utils.di

import android.content.Context
import androidx.room.Room
import com.example.mealplan.data.db.MealAppDataBase
import com.example.mealplan.data.db.MealTypeConverters
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.db.entity.WeeklyMealEntity
import com.example.mealplan.utils.Constants
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    fun provideTypeConverterHelper(gson: Gson)=MealTypeConverters(gson)
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context,typeConverters: MealTypeConverters) =
        Room.databaseBuilder(context, MealAppDataBase::class.java, Constants.DB_NAME).addTypeConverter(typeConverters)
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()


    @Provides
    @Singleton
    fun provideDao(db: MealAppDataBase) = db.dao()
    @Provides
    @Singleton
    fun provideEntity() = DailyMealEntity()
    @Provides
    @Singleton
    fun provideWeeklyEntity() = WeeklyMealEntity()
}