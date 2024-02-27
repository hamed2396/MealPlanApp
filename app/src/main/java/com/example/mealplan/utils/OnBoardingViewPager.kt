package com.example.mealplan.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealplan.ui.unboadring.SignUpFragment
import com.example.mealplan.ui.unboadring.MakeFoodFragment
import com.example.mealplan.ui.unboadring.PlanFoodFragment
import javax.inject.Inject

class OnBoardingViewPager @Inject constructor(
    manager: FragmentManager,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->MakeFoodFragment()
            1->PlanFoodFragment()
            else->SignUpFragment()
        }
    }
}