package com.example.mealplan.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.detail.StepsAdapter
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.databinding.FragmentStepBinding
import com.example.mealplan.utils.Constants.STEPS_COUNT
import com.example.mealplan.utils.Constants.STEPS_COUNT_STEP_FRAGMENT
import com.example.mealplan.utils.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepFragment :
    BaseBottomSheetDialogFragment<FragmentStepBinding>(FragmentStepBinding::inflate) {
    @Inject
    lateinit var stepsAdapter: StepsAdapter
    private val args by navArgs<StepFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        STEPS_COUNT_STEP_FRAGMENT?.let {
            STEPS_COUNT = it

        }
        val arg = args.stepList as ResponseRandomMeal.Result.AnalyzedInstruction
        arg.let {
            it.steps?.let { list ->

                stepsAdapter.setData(list)
                binding.stepList.initRecyclerViewAdapter(stepsAdapter)

            }
        }

    }

    override fun getTheme() = R.style.RemoveDialogBackground

}