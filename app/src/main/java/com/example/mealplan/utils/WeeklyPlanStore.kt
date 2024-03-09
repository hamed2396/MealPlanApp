package com.example.mealplan.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class WeeklyPlanStore @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val FIRST_TIME = "firstTime"
        private const val DS_NAME = "weeklyPlan"

        private val Context.dataStore by preferencesDataStore(DS_NAME)
        private val firstTime = booleanPreferencesKey(FIRST_TIME)

    }

    suspend fun saveUserEntrance(firstTimeEnter: Boolean) = context.dataStore.edit {
        it[firstTime] = firstTimeEnter

    }

    fun hasUserEntered() = context.dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map {
        it[firstTime] ?: false
    }

    suspend fun clearAll() = context.dataStore.edit { it.clear() }
}