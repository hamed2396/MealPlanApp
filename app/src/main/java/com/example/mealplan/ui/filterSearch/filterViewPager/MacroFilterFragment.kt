package com.example.mealplan.ui.filterSearch.filterViewPager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.disable
import com.crazylegend.kotlinextensions.views.enable
import com.example.mealplan.databinding.FragmentMacroFilterBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.Constants.isSwitchEnabled
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.viewmodel.FilterSearchViewModel
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MacroFilterFragment :
    BaseFragment<FragmentMacroFilterBinding>(FragmentMacroFilterBinding::inflate) {
    private var minCalRange: String? = null
    private var maxCalRange: String? = null
    private var minCarbRange: String? = null
    private var maxCarbRange: String? = null
    private var minProteinRange: String? = null
    private var maxProteinRange: String? = null
    private var minFatRange: String? = null
    private var maxFatRange: String? = null
    private val filterSearchViewModel by activityViewModels<FilterSearchViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalRange()
        initCarbRange()
        initProteinRange()
        initFatRange()
        enableOrDisableSliders()


    }


    @SuppressLint("SetTextI18n")
    private fun initCalRange() {
        binding.apply {
            txtCalRange.text = "1 - 400 Calories"
            calRange.apply {
                setValues(1f, 400f)
                addOnChangeListener { slider, _, _ ->
                    val values = slider.values
                    minCalRange = values[0].toInt().toString().trim()
                    maxCalRange = values[1].toInt().toString().trim()
                    txtCalRange.text = "$minCalRange - $maxCalRange Calories"
                }
                addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: RangeSlider) {

                    }

                    override fun onStopTrackingTouch(slider: RangeSlider) {
                        val values = slider.values
                        minCalRange = values[0].toInt().toString().trim()
                        filterSearchViewModel.minCalRange = minCalRange!!
                        maxCalRange = values[1].toInt().toString().trim()
                        filterSearchViewModel.maxCalRange = maxCalRange!!
                    }
                })


            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initCarbRange() {
        binding.apply {
            txtCarbRange.text = "1 - 20 carbs"
            carbRange.apply {
                setValues(1f, 20f)
                addOnChangeListener { slider, _, _ ->
                    val values = slider.values
                    minCarbRange = values[0].toInt().toString().trim()
                    maxCarbRange = values[1].toInt().toString().trim()
                    txtCarbRange.text = "$minCarbRange - $maxCarbRange carbs"


                }
                addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: RangeSlider) {

                    }

                    override fun onStopTrackingTouch(slider: RangeSlider) {
                        val values = slider.values
                        minCarbRange = values[0].toInt().toString().trim()
                        filterSearchViewModel.minCarbRange = minCarbRange!!
                        maxCarbRange = values[1].toInt().toString().trim()
                        filterSearchViewModel.maxCarbRange = maxCarbRange!!
                    }
                })


            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initProteinRange() {
        binding.apply {
            txtProteinRange.text = "1 - 20 protein"
            proteinRange.apply {
                setValues(1f, 20f)
                addOnChangeListener { slider, _, _ ->
                    val values = slider.values
                    minProteinRange = values[0].toInt().toString().trim()
                    maxProteinRange = values[1].toInt().toString().trim()
                    txtProteinRange.text = "$minProteinRange - $maxProteinRange protein"


                }
                addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: RangeSlider) {

                    }

                    override fun onStopTrackingTouch(slider: RangeSlider) {
                        val values = slider.values
                        minProteinRange = values[0].toInt().toString().trim()
                        filterSearchViewModel.minProteinRange = minProteinRange!!
                        maxProteinRange = values[1].toInt().toString().trim()
                        filterSearchViewModel.maxProteinRange = maxProteinRange!!
                    }
                })


            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initFatRange() {
        binding.apply {
            txtFatRange.text = "1 - 20 fat"
            fatRange.apply {
                setValues(1f, 20f)
                addOnChangeListener { slider, _, _ ->
                    val values = slider.values
                    minFatRange = values[0].toInt().toString().trim()
                    maxFatRange = values[1].toInt().toString().trim()
                    txtFatRange.text = "$minFatRange - $maxFatRange fat"


                }
                addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: RangeSlider) {

                    }

                    override fun onStopTrackingTouch(slider: RangeSlider) {
                        val values = slider.values
                        minFatRange = values[0].toInt().toString().trim()
                        filterSearchViewModel.minFatRange = minFatRange!!
                        maxFatRange = values[1].toInt().toString().trim()
                        filterSearchViewModel.maxFatRange = maxFatRange!!
                    }
                })


            }
        }

    }

    private fun disableSliders(enable: Boolean, vararg sliders: RangeSlider) {
        Constants.isFiltersEnabled.value = enable
        sliders.forEach {
            if (enable) {
                it.apply {
                    enable()
                    alpha = 1f
                }
            } else {
                it.apply {
                    disable()
                    alpha = .5f
                }
            }
        }
    }

    private fun enableOrDisableSliders() {
        binding.apply {
            toggleAccess.isChecked= isSwitchEnabled
            disableSliders(
                isSwitchEnabled,
                fatRange,
                proteinRange,
                calRange,
                carbRange
            )
            toggleAccess.setOnCheckedChangeListener { _, isChecked ->
                isSwitchEnabled =isChecked
                disableSliders(
                    isSwitchEnabled,
                    fatRange,
                    proteinRange,
                    calRange,
                    carbRange
                )
            }
        }
    }

    override fun onDestroyView() {
        "mdddacro".logError()
        super.onDestroyView()
    }
}

