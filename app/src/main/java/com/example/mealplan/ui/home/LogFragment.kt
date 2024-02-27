package com.example.mealplan.ui.home

import android.os.Bundle
import android.view.View
import com.example.mealplan.databinding.FragmentLogBinding
import com.example.mealplan.databinding.FragmentMealBinding
import com.example.mealplan.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogFragment : BaseFragment<FragmentLogBinding>(FragmentLogBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
    }
}