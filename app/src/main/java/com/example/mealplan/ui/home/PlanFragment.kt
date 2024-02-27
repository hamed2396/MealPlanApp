package com.example.mealplan.ui.home

import android.os.Bundle
import android.view.View
import com.example.mealplan.databinding.FragmentPlanBinding
import com.example.mealplan.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanFragment : BaseFragment<FragmentPlanBinding>(FragmentPlanBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
    }
}