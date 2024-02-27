package com.example.mealplan.ui.filterSearch.filterViewPager

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.mealplan.R
import com.example.mealplan.databinding.FragmentRecipeFilterBinding
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.autoSelectChip
import com.example.mealplan.utils.extensions.setupChip
import com.example.mealplan.viewmodel.FilterSearchViewModel
import com.example.mealplan.viewmodel.SearchViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFilterFragment :
    BaseFragment<FragmentRecipeFilterBinding>(FragmentRecipeFilterBinding::inflate) {
    private val searchViewModel by viewModels<SearchViewModel>()
    private val filterSearchViewModel by activityViewModels<FilterSearchViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val intolerancesChip =
                expandableIntolerances.secondLayout.findViewById<ChipGroup>(R.id.chipGroup)
            val dietChips = expandableDiets.secondLayout.findViewById<ChipGroup>(R.id.chipGroup)
            val cuisineChips =
                expandableCuisines.secondLayout.findViewById<ChipGroup>(R.id.chipGroup)
            setupCuisine(cuisineChips)
            setupDiet(dietChips)
            setupIntolerances(intolerancesChip)



            autoSelectChips(
                intolerancesChip to filterSearchViewModel.intolerance,
                dietChips to filterSearchViewModel.diet,
                cuisineChips to filterSearchViewModel.cuisine
            )


        }


    }


    private fun setupCuisine(chip: ChipGroup) {
        binding.expandableCuisines.apply {
            parentLayout.findViewById<TextView>(R.id.txtTitle).text = getString(R.string.cuisine)
            val txtVisibility = parentLayout.findViewById<TextView>(R.id.txtVisibility)
            parentLayout.setOnClickListener {
                if (isExpanded) {
                    collapse()
                    txtVisibility.text = getString(R.string.show)
                } else {
                    txtVisibility.text = getString(R.string.hide)
                    expand(LayoutParams.MATCH_PARENT)
                }
            }
            chip.setupChip(searchViewModel.cuisinesList())
            chipCuisineClickListeners(chip)
        }
    }

    private fun setupDiet(chip: ChipGroup) {
        binding.expandableDiets.apply {
            parentLayout.findViewById<TextView>(R.id.txtTitle).text =
                getString(R.string.select_diet)
            val txtVisibility = parentLayout.findViewById<TextView>(R.id.txtVisibility)
            parentLayout.setOnClickListener {
                if (isExpanded) {
                    collapse()
                    txtVisibility.text = getString(R.string.show)
                } else {
                    txtVisibility.text = getString(R.string.hide)
                    expand(LayoutParams.MATCH_PARENT)
                }
            }
            chip.setupChip(searchViewModel.dietList())
            chipDietClickListeners(chip)
        }
    }

    private fun setupIntolerances(chip: ChipGroup) {
        binding.expandableIntolerances.apply {
            parentLayout.findViewById<TextView>(R.id.txtTitle).text =
                getString(R.string.intolerance)
            val txtVisibility = parentLayout.findViewById<TextView>(R.id.txtVisibility)
            parentLayout.setOnClickListener {
                if (isExpanded) {
                    collapse()
                    txtVisibility.text = getString(R.string.show)
                } else {
                    txtVisibility.text = getString(R.string.hide)
                    expand(LayoutParams.MATCH_PARENT)
                }
            }
            chip.setupChip(searchViewModel.intolerancesList())
            chipIntoleranceClickListeners(chip)
        }
    }


    private fun chipDietClickListeners(chipDiet: ChipGroup) {
        binding.apply {
            chipDiet.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    filterSearchViewModel.diet = chip.tag.toString()


                }
            }
        }
    }

    private fun chipCuisineClickListeners(chipCuisine: ChipGroup) {
        binding.apply {
            chipCuisine.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    filterSearchViewModel.cuisine = chip.tag.toString()


                }
            }
        }
    }

    private fun chipIntoleranceClickListeners(chipIntolerance: ChipGroup) {
        binding.apply {
            chipIntolerance.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    filterSearchViewModel.intolerance = chip.tag.toString()


                }
            }
        }
    }


    private fun autoSelectChips(vararg chipPairs: Pair<ChipGroup, String?>) {
        chipPairs.forEach { (chipGroup, chipText) ->
            chipGroup.autoSelectChip(chipText)
        }

    }
}









