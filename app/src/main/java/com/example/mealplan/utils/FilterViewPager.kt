package com.example.mealplan.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealplan.ui.filterSearch.filterViewPager.RecipeFilterFragment
import com.example.mealplan.ui.filterSearch.filterViewPager.MacroFilterFragment
import javax.inject.Inject

class FilterViewPager @Inject constructor(
    manager: FragmentManager,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->MacroFilterFragment()
            else->RecipeFilterFragment()

        }
    }
}