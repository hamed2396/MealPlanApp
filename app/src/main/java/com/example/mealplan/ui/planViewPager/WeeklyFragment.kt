package com.example.mealplan.ui.planViewPager

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.WeeklyAdapter
import com.example.mealplan.data.db.entity.WeeklyMealEntity
import com.example.mealplan.data.models.ResponseWeeklyMeals
import com.example.mealplan.databinding.FragmentWeeklyBinding
import com.example.mealplan.ui.home.PlanFragmentDirections
import com.example.mealplan.utils.WeeklyPlanStore
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.extensions.showChangePlan
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.PlanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class WeeklyFragment :
    BaseFragment<FragmentWeeklyBinding>(FragmentWeeklyBinding::inflate) {
    private lateinit var callback: OnBackPressedCallback


    @Inject
    lateinit var saturdayAdapter: WeeklyAdapter

    @Inject
    lateinit var sundayAdapter: WeeklyAdapter

    @Inject
    lateinit var mondayAdapter: WeeklyAdapter

    @Inject
    lateinit var tuesdayAdapter: WeeklyAdapter

    @Inject
    lateinit var wednesdayAdapter: WeeklyAdapter

    @Inject
    lateinit var thursdayAdapter: WeeklyAdapter

    @Inject
    lateinit var fridayAdapter: WeeklyAdapter

    @Inject
    lateinit var weeklyPlanStore: WeeklyPlanStore

    @Inject
    lateinit var entity: WeeklyMealEntity
    private val viewModel by viewModels<PlanViewModel>()
    private lateinit var parentPager: ViewPager2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentPager =
            requireParentFragment().requireView().findViewById(R.id.viewPager)
        launchLifeCycleScope {
            weeklyPlanStore.hasUserEntered()
                .first() // Collect only the first emitted value
                .let { hasUserEntered ->
                    if (hasUserEntered) {

                        viewModel.getAllWeeklyPlan()
                    } else {

                        viewModel.callWeeklyMeals()
                    }
                }
        }
        binding.imgChange.setOnClickListener {
            it.showChangePlan {
                viewModel.callWeeklyMeals()
                dismiss()
            }
        }



        controlBackPress()
        setupRecyclerViews()
        loadPlanFromDb()
    }


    private fun controlBackPress() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val pageNumber = parentPager.currentItem
                if (pageNumber == 1) parentPager.currentItem =
                    0 else findNavController().popBackStack()

            }
        }

        // Add the callback to the back press dispatcher
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        //img back click
        binding.imgBack.setOnClickListener {
            parentFragment?.parentFragment?.view?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem =
                0
        }
    }

    private fun setupRecyclerViews() {
        binding.apply {
            viewModel.weekMeals.observe(viewLifecycleOwner) { status ->
                when (status) {
                    is NetworkStatus.Error -> {
                        loading.gone()
                    }

                    is NetworkStatus.Loading -> {
                        recyclerViewContainer.gone()
                        loading.visible()
                    }

                    is NetworkStatus.Success -> {
                        loading.gone()
                        recyclerViewContainer.visible()
                        launchLifeCycleScope {
                            weeklyPlanStore.saveUserEntrance(true)
                        }
                        entity.week = status.success
                        viewModel.deleteWeeklyPlanDb()
                        viewModel.insertPlanToDb(entity)
                        initSaturdayList(status.success)
                        initSundayList(status.success)
                        initMondayList(status.success)
                        initTuesdayList(status.success)
                        initWednesdayList(status.success)
                        initThursdayList(status.success)
                        initFridayList(status.success)


                    }

                }
            }
        }
    }

    private fun initSaturdayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            saturdayAdapter.setSaturdayData(data)
            saturdayList.initRecyclerViewAdapter(
                saturdayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            saturdayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }

        }
    }

    private fun initSundayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            sundayAdapter.setSundayData(data)
            sundayList.initRecyclerViewAdapter(
                sundayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            sundayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }

        }
    }

    private fun initMondayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            mondayAdapter.setMondayData(data)
            mondayList.initRecyclerViewAdapter(
                mondayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            mondayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun initTuesdayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            tuesdayAdapter.setTuesdayData(data)
            tuesdayList.initRecyclerViewAdapter(
                tuesdayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            tuesdayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun initWednesdayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            wednesdayAdapter.setWednesdayData(data)
            wednesdayList.initRecyclerViewAdapter(
                wednesdayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            wednesdayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun initThursdayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            thursdayAdapter.setThursdayData(data)
            thursdayList.initRecyclerViewAdapter(
                thursdayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            thursdayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun initFridayList(data: ResponseWeeklyMeals?) {
        binding.apply {
            fridayAdapter.setFridayData(data)
            fridayList.initRecyclerViewAdapter(
                fridayAdapter,
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            fridayAdapter.setOnItemClickListener {
                PlanFragmentDirections.actionToNavDetail(it.id!!).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    private fun loadPlanFromDb() {
        viewModel.weeklyItems.observe(viewLifecycleOwner) { status ->
            binding.apply {
                when (status) {
                    is NetworkStatus.Error -> {
                        root.snackbar(status.error!!)
                    }

                    is NetworkStatus.Loading -> {}
                    is NetworkStatus.Success -> {
                        loading.gone()
                        recyclerViewContainer.visible()
                        initSaturdayList(status.success?.week)
                        initSundayList(status.success?.week)
                        initMondayList(status.success?.week)
                        initTuesdayList(status.success?.week)
                        initWednesdayList(status.success?.week)
                        initThursdayList(status.success?.week)
                        initFridayList(status.success?.week)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        callback.remove()
        super.onDestroyView()
    }
}



