package com.example.mealplan.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.activity.setStatusBarColor
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.meal_fragment.RandomMealAdapter
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.databinding.FragmentMealBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.commaSeparator
import com.example.mealplan.utils.extensions.infiniteSnackBar
import com.example.mealplan.utils.extensions.isBiggerThan
import com.example.mealplan.utils.extensions.isLessThan
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.MealViewModel
import com.example.mealplan.viewmodel.PlanViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MealFragment : BaseFragment<FragmentMealBinding>(FragmentMealBinding::inflate) {

    @Inject
    lateinit var randomMealAdapter: RandomMealAdapter
    private val viewModel by activityViewModels<MealViewModel>()
    private val planViewModel by viewModels<PlanViewModel>()
    private var calAmount = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setStatusBarColor(compatColor(R.color.greenLizard))
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            planViewModel.getAllMeals()
            viewModel.provideQueries()
            txtRestaurant.setOnClickListener { findNavController().navigate(R.id.actionNavMainToNavRestaurant) }
            //hide or shrink fab button
            randomList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if ((dy isBiggerThan 0) and (btnFab.isExtended)) {
                        btnFab.shrink()

                    } else if ((dy isLessThan 0) and (btnFab.isExtended).not()) {
                        btnFab.extend()
                    }
                }
            })
            //load data
            loaRandomData()
            mealTypeClickHandler()
            showCaloriesInFab()


        }
    }

    @SuppressLint("SetTextI18n")
    private fun showCaloriesInFab() {

        planViewModel.getAllMeals.observe(viewLifecycleOwner) { entities ->
            if (entities.isNullOrEmpty()) calAmount = 0
            else {
                calAmount = 0
                entities.forEach {
                    it.calories?.let { amount ->

                        calAmount += amount
                    }

                }
            }

            if (calAmount != 0) {
                binding.btnFab.text = "${calAmount.commaSeparator} Cal"
            }
        }
    }


    private fun loaRandomData() {
        binding.apply {
            viewModel.randomMeals.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        root.infiniteSnackBar(
                            it.error!!,
                            actionName = getString(R.string.retry),
                            action = { viewModel.getRandomMeals(viewModel.provideQueries()) },
                            actionColor = R.color.mayaBlue
                        )
                        randomList.hideShimmer()
                    }

                    is NetworkStatus.Loading -> {
                        randomList.showShimmer()

                    }

                    is NetworkStatus.Success -> {
                        randomList.hideShimmer()
                        initRandomRecyclerView(it.success?.results!!)

                    }
                }
            }
        }
    }


    private fun initRandomRecyclerView(data: List<ResponseRandomMeal.Result>) {
        binding.apply {
            randomMealAdapter.setData(data)
            randomList.initRecyclerViewAdapter(randomMealAdapter)
            randomMealAdapter.setOnItemClickListener { id, step ->
                MealFragmentDirections.actionDetail(id, step!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun mealTypeClickHandler() {
        binding.apply {
            bread.setOnClickListener {
                MealFragmentDirections.actionMealToSearch(Constants.BREAD).apply {
                    findNavController().navigate(this)
                }
            }
            snack.setOnClickListener {
                MealFragmentDirections.actionMealToSearch(Constants.SNACK).apply {
                    findNavController().navigate(this)
                }
            }
            breakfast.setOnClickListener {
                MealFragmentDirections.actionMealToSearch(Constants.BREAKFAST).apply {
                    findNavController().navigate(this)
                }
            }
            soup.setOnClickListener {
                MealFragmentDirections.actionMealToSearch(Constants.SOUP).apply {
                    findNavController().navigate(this)
                }
            }
            drink.setOnClickListener {
                MealFragmentDirections.actionMealToSearch(Constants.DRINK).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }


}