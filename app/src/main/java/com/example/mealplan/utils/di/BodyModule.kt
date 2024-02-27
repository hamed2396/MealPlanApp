package com.example.mealplan.utils.di

import com.example.mealplan.data.models.signup.BodySignUp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BodyModule {
    @Provides
    fun provideBody() = BodySignUp()
}