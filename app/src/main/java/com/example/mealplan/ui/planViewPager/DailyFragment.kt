package com.example.mealplan.ui.planViewPager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.DailyAdapter
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.databinding.FragmentDailyBinding
import com.example.mealplan.ui.home.PlanFragmentDirections
import com.example.mealplan.utils.Constants.BREAKFAST
import com.example.mealplan.utils.Constants.DINNER
import com.example.mealplan.utils.Constants.LUNCH
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.events.Event
import com.example.mealplan.utils.events.EventBus
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.viewmodel.PlanViewModel
import com.skydoves.expandablelayout.ExpandableLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class DailyFragment :
    BaseFragment<FragmentDailyBinding>(FragmentDailyBinding::inflate) {
    private lateinit var breakFastLay: ExpandableLayout
    private lateinit var lunchLay: ExpandableLayout
    private lateinit var dinnerLay: ExpandableLayout
    private val viewModel by viewModels<PlanViewModel>()

    @Inject
    lateinit var breakFastAdapter: DailyAdapter

    @Inject
    lateinit var lunchAdapter: DailyAdapter

    @Inject
    lateinit var dinnerAdapter: DailyAdapter


    @Inject
    lateinit var entity: DailyMealEntity
    private lateinit var countDownTimer: CountDownTimer
    private var deleteItemTitle = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCountDownTimer()
        initExpandableViews()
        //check IF THERES ANY RECORD in db
        checkDbItems()
        handleItemDeleted()
        viewModel.getAllMeals()
        launchLifeCycleScope {
            EventBus.subscribe<Event.DoneWorker>(this) {
                viewModel.getAllMeals()
            }
        }
        //go to weekly
        binding.fabNext.setOnClickListener {
            parentFragment?.parentFragment?.view?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem =
                1
        }


    }

    private fun setupCountDownTimer() {
        val currentTime = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (currentTime.after(this)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // Calculate remaining time until midnight
        val remainingTime = midnight.timeInMillis - currentTime.timeInMillis

        // Initialize countdown timer
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
                val hours = remainingSeconds / 3600
                val minutes = (remainingSeconds % 3600) / 60
                val seconds = remainingSeconds % 60

                // Update TextViews
                binding.txtHour.text = "${String.format("%02d", hours)}h"
                binding.txtMinutes.text = "${String.format("%02d", minutes)}m"
                binding.txtSeconds.text = "${String.format("%02d", seconds)}s"
            }

            override fun onFinish() {
                // Handle timer finish, if needed
                countDownTimer.cancel()
            }
        }

        // Start countdown timer
        countDownTimer.start()
    }


    private fun initExpandableViews() {
        binding.apply {
            breakFastLay = exBreakFast.findViewById(R.id.exBreakFast)
            lunchLay = exLunch.findViewById(R.id.exLunch)
            dinnerLay = exDinner.findViewById(R.id.exDinner)
            handleExpandableOnClickListener()
            onExpandListeners()
        }
    }

    private fun handleExpandableOnClickListener() {

        breakFastLay.apply {
            setOnClickListener {
                if (breakFastLay.isExpanded) {
                    collapse()
                } else {
                    expand()
                }
            }
        }
        lunchLay.apply {
            setOnClickListener {
                if (lunchLay.isExpanded) {
                    collapse()
                } else {
                    expand()
                }
            }
        }

        dinnerLay.apply {
            setOnClickListener {
                if (dinnerLay.isExpanded) {
                    collapse()
                } else {
                    expand()
                }
            }
        }
    }


    private fun onExpandListeners() {
        breakFastLay.setOnExpandListener {
            if (it.not()) {
                //init Recyclers
                initBreakFastRecycler()
            }
        }
        lunchLay.setOnExpandListener {
            if (it.not()) {
                //init Recyclers
                initLunchRecycler()
            }
        }
        dinnerLay.setOnExpandListener {
            if (it.not()) {
                initDinnerRecycler()

            }
        }
    }

    private fun initBreakFastRecycler() {
        val rv = breakFastLay.secondLayout.findViewById<RecyclerView>(R.id.recyclerViewByTime)
        val list = viewModel.getMealByTime(BREAKFAST)

        breakFastAdapter.setData(list)
        rv.initRecyclerViewAdapter(
            breakFastAdapter,
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            false
        )
        breakFastAdapter.setOnItemClickListener { entity, delete ->
            if (delete) {

                viewModel.deleteDailyToDb(entity)
                launchLifeCycleScope {
                    delay(100)
                    deleteItemTitle = viewModel.observeMealDelete(entity.mealTime!!)
                }
            } else {
                PlanFragmentDirections.actionToNavDetail(entity.id!!).apply {
                    findNavController().navigate(this)

                }
            }

        }
    }

    private fun initLunchRecycler() {
        val rv = lunchLay.secondLayout.findViewById<RecyclerView>(R.id.recyclerViewByTime)
        val list = viewModel.getMealByTime(LUNCH)
        lunchAdapter.setData(list)
        rv.initRecyclerViewAdapter(
            lunchAdapter,
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            false
        )
        lunchAdapter.setOnItemClickListener { entity, delete ->
            if (delete) {

                viewModel.deleteDailyToDb(entity)
                launchLifeCycleScope {
                    delay(100)
                    deleteItemTitle = viewModel.observeMealDelete(entity.mealTime!!)
                }
            } else {
                PlanFragmentDirections.actionToNavDetail(entity.id!!).apply {
                    findNavController().navigate(this)

                }
            }

        }
    }

    private fun initDinnerRecycler() {
        val rv = dinnerLay.secondLayout.findViewById<RecyclerView>(R.id.recyclerViewByTime)
        val list = viewModel.getMealByTime(DINNER)
        dinnerAdapter.setData(list)
        rv.initRecyclerViewAdapter(
            dinnerAdapter,
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            false
        )
        dinnerAdapter.setOnItemClickListener { entity, delete ->

            if (delete) {

                viewModel.deleteDailyToDb(entity)
                launchLifeCycleScope {
                    delay(100)
                    deleteItemTitle = viewModel.observeMealDelete(entity.mealTime!!)
                }
            } else {
                PlanFragmentDirections.actionToNavDetail(entity.id!!).apply {
                    findNavController().navigate(this)

                }
            }

        }
    }

    private fun checkDbItems() {

        viewModel.getAllMeals.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.lottie.apply {
                    visible()
                    playAnimation()
                }
                binding.apply {
                    topContainer.gone()
                    exContainer.gone()
                }
            } else {
                binding.lottie.apply {
                    gone()
                    cancelAnimation()
                }
                binding.apply {
                    topContainer.visible()
                    exContainer.visible()

                }
            }

        }
    }

    private fun handleItemDeleted() {
        binding.apply {
            viewModel.mealDelete.observe(viewLifecycleOwner) {
                it.logError()
                deleteItemTitle.logError()
                when (deleteItemTitle) {
                    BREAKFAST -> {
                        if (it.isNotNullOrEmpty){

                            breakFastAdapter.setData(it)
                        }
                    }
                    LUNCH    -> {
                        if (it.isNotNullOrEmpty){

                            lunchAdapter.setData(it)
                        }

                    }
                    DINNER -> {
                        if (it.isNotNullOrEmpty){

                            dinnerAdapter.setData(it)
                        }

                    }
                }
            }
        }
    }

    override fun onDestroyView() {

        if (::countDownTimer.isInitialized) countDownTimer.cancel()
        super.onDestroyView()
    }
}



