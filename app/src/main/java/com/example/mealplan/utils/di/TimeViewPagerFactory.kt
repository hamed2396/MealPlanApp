package com.example.mealplan.utils.di

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.mealplan.utils.TimeViewPager
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TimeViewPagerFactory {
    fun create(manager: FragmentManager, lifecycle: Lifecycle): TimeViewPager
}
