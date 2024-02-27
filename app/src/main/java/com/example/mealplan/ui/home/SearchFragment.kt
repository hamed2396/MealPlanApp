package com.example.mealplan.ui.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.activity.hideKeyboardSoft
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.addDebounceTextListener
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.snackbarLong
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.meal_fragment.RandomMealAdapter
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.databinding.FragmentSearchBinding
import com.example.mealplan.utils.Constants.hasAppliedFilters
import com.example.mealplan.utils.Constants.hasClearedFilters
import com.example.mealplan.utils.Constants.isSwitchEnabled
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.events.Event
import com.example.mealplan.utils.events.EventBus
import com.example.mealplan.utils.extensions.autoSelectChip
import com.example.mealplan.utils.extensions.infiniteSnackBar
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.FilterSearchViewModel
import com.example.mealplan.viewmodel.SearchViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val args by navArgs<SearchFragmentArgs>()
    private val viewModel by viewModels<SearchViewModel>()
    private val filterSearchViewModel by activityViewModels<FilterSearchViewModel>()

    @Inject
    lateinit var searchMealAdapter: RandomMealAdapter
    private var chipText: String? = null
    private var enteredSearch: String? = null
    private var minCalRange: String? = null
    private var maxCalRange: String? = null
    private var minCarbRange: String? = null
    private var maxCarbRange: String? = null
    private var minProteinRange: String? = null
    private var maxProteinRange: String? = null
    private var minFatRange: String? = null
    private var maxFatRange: String? = null
    private var intolerance: String? = null
    private var diet: String? = null
    private var cuisine: String? = null
    private lateinit var chipMealType: Chip


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.imgBack.setOnClickListener { findNavController().navigate(R.id.mealFragment) }

            if (args.type.isNotNullOrEmpty()) {
                chipText = args.type
                viewModel.callSearchMeals(viewModel.provideQueries(chipText?.lowercase()))
            }
            //search edt click listener
            setOnDrawableClickListener()

            setupChip(viewModel.mealsList(), mealChipGroup)
            //auto scroll to chip
            mealChipGroup.autoSelectChip(chipText)
            //load api call
            loadSearchData()

            chipClickListeners()
            listenToEditText()
            handleAppliedOrClearedFilters()

        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setOnDrawableClickListener() {
        binding.apply {
            edtSearch.setOnTouchListener { _, event ->
                val drawableRight = 2
                val drawableEnd = edtSearch.compoundDrawables[drawableRight]
                if (event.action == MotionEvent.ACTION_UP) {
                    drawableEnd?.let {
                        val drawableWidth = it.bounds.width()
                        val drawableRightBound = edtSearch.right - edtSearch.paddingEnd
                        val drawableLeftBound = drawableRightBound - drawableWidth
                        if (event.rawX >= drawableLeftBound) {

                            findNavController().navigate(R.id.actionNavMainToNavFilter)
                            // Hide the keyboard
                            requireActivity().hideKeyboardSoft()

                            return@setOnTouchListener true
                        }
                    }
                }
                false
            }
        }

    }

    private fun chipClickListeners() {
        binding.apply {
            mealChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

                checkedIds.forEach {
                    chipMealType = group.findViewById(it)
                    val query =
                        viewModel.provideQueries(
                            chipMealType.text.toString().lowercase(),
                            enteredSearch,
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

                    viewModel.callSearchMeals(query)


                }
            }
        }
    }

    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        list.forEach {
            val state = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                intArrayOf(compatColor(R.color.white), compatColor(R.color.limedSpruce))
            )
            val drawable =
                ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.greenChip)
            Chip(requireContext()).also { chip ->
                chip.setChipDrawable(drawable)
                chip.text = it
                chip.setTextColor(state)
                chip.tag = it
                view.addView(chip)
                view.post {
                    chipText?.let { txt -> scrollToChip(binding.mealChipsScroll, view, txt) }
                }
            }

        }
    }


    private fun scrollToChip(
        horizontalScrollView: HorizontalScrollView,
        chipGroup: ChipGroup,
        chipName: String
    ) {
        horizontalScrollView.isSmoothScrollingEnabled = true
        val chip = chipGroup.findViewWithTag<Chip>(chipName)
        if (chip != null) {
            val chipIndex = chipGroup.indexOfChild(chip)
            val chipView = chipGroup.getChildAt(chipIndex)

            // Calculate the scroll position to bring the chip into view horizontally
            val scrollX =
                chipView.left - horizontalScrollView.paddingLeft - (horizontalScrollView.width - chipView.width) / 2

            // Scroll the HorizontalScrollView to bring the chip into view
            horizontalScrollView.post {
                horizontalScrollView.smoothScrollTo(scrollX, 0)
            }
        } else {
            //handle error
        }
    }

    private fun loadSearchData() {
        binding.apply {
            viewModel.searchMeals.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        val query = viewModel.provideQueries(
                            chipMealType.text.toString().lowercase(),
                            enteredSearch,
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
                        root.infiniteSnackBar(
                            it.error!!,
                            getString(R.string.retry),
                            action = { viewModel.callSearchMeals(query) },
                            R.color.mayaBlue
                        )
                        mealList.hideShimmer()
                    }

                    is NetworkStatus.Loading -> {
                        mealList.showShimmer()

                    }

                    is NetworkStatus.Success -> {
                        mealList.hideShimmer()
                        it.success?.results?.isEmpty()?.ifTrue {
                            root.snackbarLong(getString(R.string.nothing_found))
                        }
                        initRandomRecyclerView(it.success?.results!!)

                    }
                }
            }
        }
    }


    private fun initRandomRecyclerView(data: List<ResponseRandomMeal.Result>) {
        binding.apply {
            searchMealAdapter.setData(data)
            mealList.initRecyclerViewAdapter(searchMealAdapter)
            searchMealAdapter.setOnItemClickListener { id, step ->
                SearchFragmentDirections.actionDetail(id, step!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun listenToEditText() {
        binding.edtSearch.addDebounceTextListener {
            enteredSearch = it.toString().ifEmpty { null }

            if (enteredSearch.isNotNullOrEmpty()) {
                val query = viewModel.provideQueries(
                    chipText,
                    enteredSearch,
                    minCalRange,
                    maxCalRange,
                    minCarbRange,
                    maxCarbRange,
                    minProteinRange,
                    maxProteinRange,
                    minFatRange,
                    maxFatRange, intolerance, diet, cuisine
                )

                viewModel.callSearchMeals(query)
            }


        }
    }

    private fun handleAppliedOrClearedFilters() {
        launchLifeCycleScope {

            EventBus.subscribe<Event.HasFilterApplied>(this) { filters ->
                if (hasAppliedFilters or hasClearedFilters) {
                    hasAppliedFilters = false
                    filters.model.also {
                        minCalRange = it.minCalRange
                        maxCalRange = it.maxCalRange
                        minCarbRange = it.minCarbRange
                        maxCarbRange = it.maxCarbRange
                        minProteinRange = it.minProteinRange
                        maxProteinRange = it.maxProteinRange
                        minFatRange = it.minFaRange
                        maxFatRange = it.maxFatRange
                        intolerance = it.intolerance
                        cuisine = it.cuisine
                        diet = it.diet

                    }
                    val query = viewModel.provideQueries(
                        chipText,
                        enteredSearch,
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
                    if (hasClearedFilters) {
                        binding.root.snackbar(getString(R.string.filters_cleared))
                        hasClearedFilters = false
                    } else {
                        viewModel.callSearchMeals(query)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isSwitchEnabled = false
        filterSearchViewModel.diet = null
        filterSearchViewModel.cuisine = null
        filterSearchViewModel.intolerance = null
    }


}