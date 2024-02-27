package com.example.mealplan.ui.unboadring

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.kotlinextensions.root.logError
import com.example.mealplan.R
import com.example.mealplan.databinding.FragmentPlanFoodBinding
import com.example.mealplan.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanFoodFragment : BaseFragment<FragmentPlanFoodBinding>(FragmentPlanFoodBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            txtNext.setOnClickListener {
                parentFragment?.view?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem = 2
            }

        }
    }
}