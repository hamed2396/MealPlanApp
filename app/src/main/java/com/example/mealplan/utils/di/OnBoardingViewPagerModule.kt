package com.example.mealplan.utils.di

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object OnBoardingViewPagerModule {
    @Provides
    @FragmentScoped
    fun provideLifeCycle(fragment: Fragment) = fragment.lifecycle

    @Provides
    @FragmentScoped
    fun provideFragmentManager(fragment: Fragment) = fragment.parentFragmentManager

}