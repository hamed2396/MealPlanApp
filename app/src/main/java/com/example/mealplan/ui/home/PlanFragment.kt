package com.example.mealplan.ui.home

import android.os.Bundle
import android.view.View
import com.example.mealplan.databinding.FragmentPlanBinding
import com.example.mealplan.utils.TimeViewPager
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.di.TimeViewPagerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlanFragment : BaseFragment<FragmentPlanBinding>(FragmentPlanBinding::inflate) {

    @Inject
    lateinit var timeViewPagerFactory: TimeViewPagerFactory
    private var timeViewPager: TimeViewPager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timeViewPagerFactory.let {
            timeViewPager = it.create(childFragmentManager, lifecycle)
            binding.viewPager.apply {
                if (adapter == null) adapter = timeViewPager
                isUserInputEnabled = false


            }
        }

    }


}