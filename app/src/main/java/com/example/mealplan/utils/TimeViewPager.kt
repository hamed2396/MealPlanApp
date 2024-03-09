package com.example.mealplan.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealplan.ui.planViewPager.DailyFragment
import com.example.mealplan.ui.planViewPager.WeeklyFragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TimeViewPager @AssistedInject constructor(
    @Assisted manager: FragmentManager,
    @Assisted lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount()=2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> DailyFragment()
            else-> WeeklyFragment()
        }
    }
}
