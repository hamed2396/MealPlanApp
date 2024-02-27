package com.example.mealplan.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mealplan.data.models.signup.SignUpModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val USERNAME = "username"
        private const val HASH = "hash"
        private const val DATA_STORE_REGISTER = "data_store_register"
        private val Context.dataStore by preferencesDataStore(DATA_STORE_REGISTER)
        private val username = stringPreferencesKey(USERNAME)
        private val hash = stringPreferencesKey(HASH)
    }

    suspend fun saveUser(user: String, hashCode: String) = context.dataStore.edit {
        it[username] = user
        it[hash] = hashCode

    }

    fun getUser() = context.dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map {
        SignUpModel(it[username] ?: "", it[hash] ?: "")
    }

    suspend fun clearUser() = context.dataStore.edit { it.clear() }
}