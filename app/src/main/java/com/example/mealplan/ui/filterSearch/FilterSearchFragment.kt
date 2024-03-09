package com.example.mealplan.ui.filterSearch

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.kotlinextensions.root.logError
import com.example.mealplan.R
import com.example.mealplan.data.models.search.SearchFilterModel
import com.example.mealplan.databinding.FragmentFilterSearchBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.Constants.hasClearedFilters
import com.example.mealplan.utils.FilterViewPager
import com.example.mealplan.utils.FullHeightBottomSheet
import com.example.mealplan.utils.base.BaseBottomSheetDialogFragment
import com.example.mealplan.utils.events.Event
import com.example.mealplan.utils.events.EventBus
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.viewmodel.FilterSearchViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

@AndroidEntryPoint
class FilterSearchFragment :
    FullHeightBottomSheet<FragmentFilterSearchBinding>(FragmentFilterSearchBinding::inflate) {
    override fun getTheme() = R.style.RemoveDialogBackground

    @Inject
    lateinit var filterViewPager: FilterViewPager
    private val viewModel by activityViewModels<FilterSearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imgBack.setOnClickListener { dismiss() }
        }
        setUpViewPager()
        sendFiltersToSearchFragment()
        clearAllFilters()


    }




    private fun setUpViewPager() {

        binding.apply {
            tabLayout.addTab(
                tabLayout.newTab().setText(getString(R.string.macro_filters))
            )
            tabLayout.addTab(
                tabLayout.newTab().setText(getString(R.string.recipe_filter))
            )
            //viewPager
            detailViewPager.adapter = filterViewPager
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) detailViewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // when not selected
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //when its selected and you click on it again

                }
            })
            //this is used when you need to swipe to change tabs and tabLayout should be aware and be changed from top
            detailViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })


        }
    }

    private fun sendFiltersToSearchFragment() {
        binding.apply {
            btnApply.setOnClickListener {
                Constants.hasAppliedFilters = true
                launchLifeCycleScope {
                    val model = provideFiltersFromViewModel()
                    model.logError()
                    EventBus.publish(Event.HasFilterApplied(model))
                }
                dismiss()
            }
        }
    }
// i used CompletableDeferred because if you don't use it, return statement runs immediately and wont return anything
    private suspend fun provideFiltersFromViewModel(): SearchFilterModel {
        val deferredModel = CompletableDeferred<SearchFilterModel>()

        launchLifeCycleScope {
            Constants.isFiltersEnabled.observe(viewLifecycleOwner) {
                val model = if (it) {
                    SearchFilterModel(
                        viewModel.minCalRange ?: "1",
                        viewModel.maxCalRange ?: "400",
                        viewModel.minCarbRange ?: "1",
                        viewModel.maxCarbRange ?: "50",
                        viewModel.minProteinRange ?: "1",
                        viewModel.maxProteinRange ?: "50",
                        viewModel.minFatRange ?: "1",
                        viewModel.maxFatRange ?: "50",
                        viewModel.intolerance,
                        viewModel.diet,
                        viewModel.cuisine
                    )
                } else {
                    viewModel.run {
                        SearchFilterModel(
                            minCalRange,
                            maxCalRange,
                            minCarbRange,
                            maxCarbRange,
                            minProteinRange,
                            maxProteinRange,
                            minFatRange,
                            maxFatRange,
                            intolerance,
                            diet,
                            cuisine
                        )
                    }
                }
                deferredModel.complete(model)
            }
        }

        return deferredModel.await()
    }
    private fun clearAllFilters() {
        binding.txtClearFilter.setOnClickListener {
            hasClearedFilters = true
            launchLifeCycleScope {
                viewModel.apply {
                    val model = SearchFilterModel(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )

                    EventBus.publish(Event.HasFilterApplied(model))
                }
            }
            dismiss()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

}