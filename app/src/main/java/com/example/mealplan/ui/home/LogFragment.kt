package com.example.mealplan.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.livedata.observeOnce
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.example.mealplan.R
import com.example.mealplan.databinding.FragmentLogBinding
import com.example.mealplan.utils.Constants.BREAKFAST
import com.example.mealplan.utils.Constants.DINNER
import com.example.mealplan.utils.Constants.LUNCH
import com.example.mealplan.utils.Constants.MAX_CAL_PER_DAY
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.isBiggerThan
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.DetailViewModel
import com.example.mealplan.viewmodel.PlanViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.BarModel


@AndroidEntryPoint
class LogFragment : BaseFragment<FragmentLogBinding>(FragmentLogBinding::inflate) {
    private val viewModel by viewModels<DetailViewModel>()
    private val planViewModel by viewModels<PlanViewModel>()
    private var breakfastCal = 0f
    private var lunchCal = 0f
    private var dinnerCal = 0f
    private var saturdayWeeklyCal = 0f
    private var sundayWeeklyCal = 0f
    private var mondayWeeklyCal = 0f
    private var tuesdayWeeklyCal = 0f
    private var wednesdayWeeklyCal = 0f
    private var thursdayWeeklyCal = 0f
    private var fridayWeeklyCal = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            viewModel.getAllMeals()
            loadAllMeals()
            planViewModel.getAllWeeklyPlan()
            loadWeeklyPlan()


        }
    }

    private fun handleBarChart() {
        binding.apply {

            barChart.addBar(
                BarModel(
                    getString(R.string.saturday),
                    saturdayWeeklyCal,
                    compatColor(R.color.greenLizard)
                )
            )
            barChart.addBar(
                BarModel(
                    getString(R.string.sunday),
                    sundayWeeklyCal,
                    compatColor(R.color.violetsAreBlue)
                )
            )
            barChart.addBar(
                BarModel(
                    getString(R.string.monday),
                    mondayWeeklyCal,
                    compatColor(R.color.royalOrange)
                )
            )
            barChart.addBar(
                BarModel(
                    getString(R.string.tuesday),
                    tuesdayWeeklyCal,
                    compatColor(R.color.mayaBlue)
                )
            )
            barChart.addBar(
                BarModel(
                    getString(R.string.wednesday),
                    wednesdayWeeklyCal,
                    compatColor(R.color.pastelRed)
                )
            )
            barChart.addBar(
                BarModel(
                    getString(R.string.thursday),
                    thursdayWeeklyCal,
                    compatColor(R.color.bananaYellow)
                )
            )
            barChart.addBar(
                BarModel(
                    getString(R.string.friday),
                    fridayWeeklyCal,
                    compatColor(R.color.violet)
                )
            )

            barChart.startAnimation()
        }
    }

    private fun loadAllMeals() {
        binding.apply {


            viewModel.getAllMeals.observeOnce() { entityList ->

                entityList?.forEach {

                    when (it.mealTime) {
                        BREAKFAST -> {
                            breakfastCal = breakfastCal.plus(it.calories!!)

                        }

                        LUNCH -> {
                            lunchCal = lunchCal.plus(it.calories!!)
                        }

                        DINNER -> {
                            dinnerCal = dinnerCal.plus(it.calories!!)
                        }

                        else -> {}
                    }

                }
                handleIfMoreCalories()
                valueBreakfast.percent = breakfastCal.div(MAX_CAL_PER_DAY).times(100)
                valueLunch.percent = lunchCal.div(MAX_CAL_PER_DAY).times(100)
                valueDinner.percent = dinnerCal.div(MAX_CAL_PER_DAY).times(100)


            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleIfMoreCalories() {
        binding.apply {
            if (breakfastCal isBiggerThan MAX_CAL_PER_DAY) {
                val amount = breakfastCal.minus(MAX_CAL_PER_DAY)
                txtBreakfastOver.apply {
                    visible()
                    text = "${amount.toInt()} More cal"
                }
            } else txtBreakfastOver.gone()
            if (lunchCal isBiggerThan MAX_CAL_PER_DAY) {
                val amount = lunchCal.minus(MAX_CAL_PER_DAY)
                txtLunchOver.apply {
                    visible()
                    text = "${amount.toInt()} More cal"
                }
            } else txtLunchOver.gone()
            if (dinnerCal isBiggerThan MAX_CAL_PER_DAY) {
                val amount = dinnerCal.minus(MAX_CAL_PER_DAY)
                txtDinnerOver.apply {
                    visible()
                    text = "${amount.toInt()} More cal"
                }
            } else txtDinnerOver.gone()
        }


    }

    private fun loadWeeklyPlan() {
        planViewModel.weeklyItems.observeOnce() {
            when (it) {
                is NetworkStatus.Error -> {}
                is NetworkStatus.Loading -> {}
                is NetworkStatus.Success -> {
                    if (it.success?.week?.week?.saturday != null){
                        binding.barChart.visible()
                        binding.txtNoWeeklyItem.gone()
                        saturdayWeeklyCal =
                            it.success.week?.week?.saturday?.nutrients?.calories?.toFloat()!!
                        sundayWeeklyCal =
                            it.success.week?.week?.sunday?.nutrients?.calories?.toFloat()!!
                        mondayWeeklyCal =
                            it.success.week?.week?.monday?.nutrients?.calories?.toFloat()!!
                        tuesdayWeeklyCal =
                            it.success.week?.week?.tuesday?.nutrients?.calories?.toFloat()!!
                        wednesdayWeeklyCal =
                            it.success.week?.week?.wednesday?.nutrients?.calories?.toFloat()!!
                        thursdayWeeklyCal =
                            it.success.week?.week?.thursday?.nutrients?.calories?.toFloat()!!
                        fridayWeeklyCal =
                            it.success.week?.week?.friday?.nutrients?.calories?.toFloat()!!
                        handleBarChart()
                    }else{
                        binding.barChart.gone()
                        binding.txtNoWeeklyItem.visible()
                    }

                }
            }
        }
    }


    override fun onDestroyView() {
        breakfastCal = 0f
        lunchCal = 0f
        dinnerCal = 0f

        super.onDestroyView()
    }
}