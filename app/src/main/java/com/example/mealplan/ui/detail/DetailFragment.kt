package com.example.mealplan.ui.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.parseAsHtml
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.load
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.ifFalse
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.detail.IngredientAdapter
import com.example.mealplan.adapters.detail.SimilarAdapter
import com.example.mealplan.adapters.detail.StepsAdapter
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.models.detail.ResponseNutrition
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.databinding.FragmentDetailBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.DeleteDataBaseWorker
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.events.Event
import com.example.mealplan.utils.events.EventBus
import com.example.mealplan.utils.extensions.infiniteSnackBar
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.extensions.loadImage
import com.example.mealplan.utils.extensions.showPlanMenu
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.paulrybitskyi.valuepicker.ValuePickerView
import com.paulrybitskyi.valuepicker.model.Item
import com.paulrybitskyi.valuepicker.model.PickerItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.skydoves.androidveil.VeilLayout
import com.skydoves.progressview.ProgressView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private var videoId = ""
    private lateinit var saturatedFatVitaminC: ProgressView
    private lateinit var sugarVitaminB3: ProgressView
    private lateinit var fiberVitaminD: ProgressView
    private lateinit var netCarbohydratesVitaminB1: ProgressView
    private var nutritionApiCalled = false
    private lateinit var responseForSaturatedFat: Triple<Float, Float, Float>
    private lateinit var responseForSugar: Triple<Float, Float, Float>
    private lateinit var responseForFiber: Triple<Float, Float, Float>
    private lateinit var responseForNetCarbohydrates: Triple<Float, Float, Float>
    private lateinit var responseForVitaminC: Triple<Float, Float, Float>
    private lateinit var responseForVitaminB3: Triple<Float, Float, Float>
    private lateinit var responseForVitaminE: Triple<Float, Float, Float>
    private lateinit var responseForVitaminB: Triple<Float, Float, Float>
    private lateinit var onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener
    private var isScrolling = true

    @Inject
    lateinit var stepsAdapter: StepsAdapter

    @Inject
    lateinit var ingredientAdapter: IngredientAdapter

    @Inject
    lateinit var entity: DailyMealEntity
    @Inject
    lateinit var similarAdapter: SimilarAdapter

    private lateinit var steps: List<ResponseRandomMeal.Result.AnalyzedInstruction.Step>

    private var expanded = MutableLiveData<Boolean>()
    private var existInDb = false
    private var mealTime: String? = ""
    private var itemsExistInDb = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setUpTabLayout()
            handleTabLayout()
            viewModel.callMealInformation(args.detailId)
            loadInfoData()
            loadSimilarData()
            checkDataBase()
            loadVideoData()
            getChartData()
            setupExpandableDailyNutrition()
            observeExpandState()
            loadNutritionData()
            viewModel.getAllMeals()
            loadAnyItemInDbExist()

            layoutExpandableNutrition.imgPlan.setOnClickListener {
                findMealTime()?.let { itTime ->
                    entity.mealTime = itTime
                }
                if (existInDb && entity.mealTime != null) {
                    viewModel.deleteDailyToDb(entity)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.deleted_from_daily_plan), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    it.showPlanMenu { time ->
                        entity.mealTime = time
                        viewModel.insertDailyToDb(entity)

                        if (!itemsExistInDb) {

                            handleTimer()

                        }
                    }
                }

            }


        }
    }

    private fun loadSimilarData() {
        binding.apply {
            viewModel.similarRecipe.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        root.infiniteSnackBar(
                            it.error!!,
                            actionName = getString(R.string.retry),
                            action = { viewModel.callSimilarRecipe(args.detailId) },
                            actionColor = R.color.mayaBlue
                        )
                        layoutExpandableNutrition.similarList.hideShimmer()
                    }

                    is NetworkStatus.Loading -> {
                        layoutExpandableNutrition.similarList.showShimmer()
                    }

                    is NetworkStatus.Success -> {
                        layoutExpandableNutrition.similarList.hideShimmer()
                        similarAdapter.setData(it.success!!)
                        layoutExpandableNutrition.similarList.initRecyclerViewAdapter(
                            similarAdapter,
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        )
                        similarAdapter.setOnItemClickListener {id->
                            DetailFragmentDirections.actionToDetail(id).apply {
                                findNavController().navigate(this)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleTimer() {
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1) // Set to tomorrow
        }
        val initialDelay = midnight.timeInMillis - now.timeInMillis

        // Schedule the periodic work request to trigger the DeleteRecordsWorker at midnight
        val deleteRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DeleteDataBaseWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        Constants.WORKER_ID = deleteRequest.id

        WorkManager.getInstance(requireContext()).enqueue(deleteRequest)

    }

    private fun loadAnyItemInDbExist() {

        viewModel.getAllMeals.observe(viewLifecycleOwner) {
            launchLifeCycleScope {

                EventBus.publish(Event.CancelWorker(it))
            }
            itemsExistInDb = it.isNotEmpty()

        }

    }


    private fun findMealTime(): String? {
        val time = viewModel.getSingleMealById(args.detailId)
        mealTime = time?.mealTime
        return mealTime
    }

    private fun checkDataBase() {
        viewModel.existMeal(args.detailId).observe(viewLifecycleOwner) {
            existInDb = it
            if (it)
                binding.layoutExpandableNutrition.imgPlan.load(R.drawable.tick)
            else
                binding.layoutExpandableNutrition.imgPlan.load(R.drawable.baseline_add_24)


        }
    }

    private fun handleTabLayout() {
        binding.apply {
            onScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
                val scrollY = scrollView.scrollY
                val tabLayoutHeight = tabLayout.height
                val nutritionContainerBottom = nutritionContainer.bottom
                val ingredientContainerBottom = ingredientContainer.bottom
                val chartContainerBottom = chartContainer.bottom

                if (scrollY < nutritionContainerBottom - tabLayoutHeight) {
                    isScrolling = true
                    tabLayout.getTabAt(0)?.select()

                } else if (scrollY < ingredientContainerBottom - tabLayoutHeight) {
                    isScrolling = true
                    tabLayout.getTabAt(1)?.select()

                } else if (scrollY < chartContainerBottom - tabLayoutHeight) {
                    isScrolling = true

                    tabLayout.getTabAt(2)?.select()

                }
                isScrolling = false
            }

            scrollView.viewTreeObserver.addOnScrollChangedListener(onScrollChangedListener)

        }
    }

    private fun setUpTabLayout() {

        binding.apply {
            tabLayout.addTab(
                tabLayout.newTab().setText(getString(R.string.log_recipe))
            )
            tabLayout.addTab(
                tabLayout.newTab().setText(getString(R.string.ingredients))
            )
            tabLayout.addTab(
                tabLayout.newTab().setText(getString(R.string.nutritions))
            )
            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (!isScrolling) {
                        binding.tabLayout.getTabAt(tab.position)?.select()
                        scrollToView(tab.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })


        }
    }

    private fun scrollToView(position: Int) {
        val viewToScrollTo = when (position) {
            0 -> binding.nutritionContainer
            1 -> binding.ingredientContainer
            2 -> binding.chartContainer
            else -> return
        }
        val scrollY = viewToScrollTo.top - binding.scrollView.getChildAt(0).top
        binding.scrollView.smoothScrollTo(0, scrollY)
    }

    private fun loadInfoData() {
        binding.apply {
            viewModel.mealInfo.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        root.infiniteSnackBar(
                            it.error!!,
                            actionName = getString(R.string.retry),
                            action = {
                                viewModel.callMealInformation(args.detailId)
                            },
                            actionColor = R.color.mayaBlue
                        )
                        //handle video
                        youtubeLoader.unVeil()
                        setPlaceholderImage()
                        loading.gone()


                    }

                    is NetworkStatus.Loading -> {
                        youtubeLoader.veil()
                        loading.visible()
                    }

                    is NetworkStatus.Success -> {
                        viewModel.callSimilarRecipe(args.detailId)
                        loading.gone()
                        //handle video

                        youtubeLoader.unVeil()
                        it.success!!.extendedIngredients?.get(0)?.name?.let { id ->
                            if (id.isNotEmpty()) {
                                viewModel.callMealVideo(id)
                            } else {
                                setPlaceholderImage()
                            }
                        }
                        //HANDLE INFO
                        binding.layoutExpandableNutrition.apply {
                            loading.gone()
                            group.visible()
                            ingredientContainer.visible()
                            txtTitle.text = it.success.title
                            txtLikes.text = it.success.aggregateLikes.toString()
                            txtHealthScore.text = it.success.healthScore.toString()
                            txtPrice.text = it.success.pricePerServing.toString()
                            txtReadMoreDesc.text = it.success.summary?.parseAsHtml()
                            handleStepList()


                            it.success?.extendedIngredients?.let { ingredient ->
                                ingredientAdapter.setData(ingredient)
                                binding.layoutIngredient.randomList.initRecyclerViewAdapter(
                                    ingredientAdapter,
                                    GridLayoutManager(
                                        requireContext(),
                                        3,
                                        GridLayoutManager.VERTICAL,
                                        false
                                    )

                                )

                            }


                        }
                        //ENTITY FOR DB
                        it.success.also {
                            entity.apply {
                                id = it.id
                                title = it.title
                                calories = it.nutrition?.nutrients?.get(0)?.amount!!.roundToInt()
                                image = it.image
                            }
                        }


                    }
                }
            }
        }
    }

    private fun handleStepList() {
        if (args.steps != null) {
            (args.steps as ResponseRandomMeal.Result.AnalyzedInstruction)?.let { list ->
                list.steps?.let { stepList ->
                    steps = stepList
                    initStepsList(steps)
                    binding.layoutExpandableNutrition.stepsShowMore.setOnClickListener {
                        if (stepList.isNotNullOrEmpty) {
                            DetailFragmentDirections.actionToStep(list).apply {
                                findNavController().navigate(this)
                            }
                        } else {
                            binding.layoutExpandableNutrition.stepsShowMore.gone()
                        }
                    }
                    //  if (args.steps as ResponseRandomMeal.Result.AnalyzedInstruction == null)


                }

            }
        } else {
            binding.layoutExpandableNutrition.stepTitle.gone()
        }

    }

    private fun initStepsList(list: List<ResponseRandomMeal.Result.AnalyzedInstruction.Step>) {
        if (list.isNotEmpty()) {
            Constants.STEPS_COUNT = if (list.size < 3) {
                list.size
            } else 3
            stepsAdapter.setData(list)
            binding.layoutExpandableNutrition.apply {
                stepsList.initRecyclerViewAdapter(stepsAdapter)
                //Show more
                if (list.size > 3) {
                    Constants.STEPS_COUNT_STEP_FRAGMENT = list.size
                    stepsShowMore.visible()
                }
            }
        }
    }

    private fun loadVideoData() {
        binding.apply {
            viewModel.mealVideo.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        youtubeLoader.unVeil()
                        root.snackbar(getString(R.string.couldn_t_load_video))
                        setPlaceholderImage()
                    }

                    is NetworkStatus.Loading -> {
                        youtubeLoader.veil()
                    }

                    is NetworkStatus.Success -> {
                        youtubeLoader.unVeil()
                        val id = it.success?.videos?.firstOrNull()?.youTubeId
                        val thumbNail = it.success?.videos?.firstOrNull()?.thumbnail
                        if (id.isNotNullOrEmpty()) {
                            youtubePlayer.visible()
                            videoId = id!!
                        } else if (thumbNail.isNotNullOrEmpty()) {
                            imgPoster.apply {
                                visible()
                                loadImage(thumbNail!!)
                            }
                        } else {
                            setPlaceholderImage()
                        }
                        initYoutubePlayer()

                    }
                }
            }
        }
    }

    private fun initYoutubePlayer() {
        binding.apply {
            lifecycle.addObserver(youtubePlayer)
            youtubePlayer.enableAutomaticInitialization

            youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }

    }

    private fun getChartData() {
        binding.layoutChartNutrition.apply {
            lifecycleScope.launch {
                when (val image = viewModel.downloadPngFile(requireContext(), args.detailId)) {
                    is NetworkStatus.Error -> {
                        shimmerLayChart.unVeil()
                        root.infiniteSnackBar(
                            image.error!!,
                            actionName = getString(R.string.retry),
                            action = {
                                launchLifeCycleScope {
                                    viewModel.downloadPngFile(requireContext(), args.detailId)
                                }
                            },
                            actionColor = R.color.mayaBlue
                        )
                    }

                    is NetworkStatus.Loading -> {
                        shimmerLayChart.veil()
                    }

                    is NetworkStatus.Success -> {
                        shimmerLayChart.unVeil()
                        binding.chartContainer.visible()
                        chart.visible()
                        chart.load(image.success)

                    }
                }

            }
        }
    }

    //call api once and only when view is expanded
    private fun observeExpandState() {
        expanded.observe(viewLifecycleOwner) {
            it.ifFalse {
                if (!nutritionApiCalled) {
                    viewModel.callMealNutrition(args.detailId)

                }

                nutritionApiCalled = true
            }
        }
    }

    private fun loadNutritionData() {
        binding.apply {
            val childLayout =
                binding.layoutExpandableNutrition.expandableNutrition.secondLayout.findViewById<VeilLayout>(
                    R.id.shimmerLay
                )
            viewModel.mealNutrition.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        childLayout.gone()
                        childLayout.unVeil()
                    }

                    is NetworkStatus.Loading -> {
                        childLayout.visible()
                        childLayout.veil()
                    }

                    is NetworkStatus.Success -> {
                        childLayout.unVeil()
                        childLayout.gone()
                        //We Need to find index because api response is giving different index for each item
                        initNutritionIndexAndValues(it.success?.nutrients!!)

                        initVitaminsIndexAndValues(it.success.nutrients)

                        binding.layoutExpandableNutrition.expandableNutrition.secondLayout.findViewById<ConstraintLayout>(
                            R.id.mainContainer
                        ).visible()
                        setDefaultNutritionProgressView()
                    }
                }
            }
        }
    }

    private fun initNutritionIndexAndValues(list: List<ResponseNutrition.Nutrient>) {
        Constants.FIBER_INDEX =
            findIndexOfNutrient(list, getString(R.string.fiber))
        Constants.NET_CARBOHYDRATES_INDEX = findIndexOfNutrient(
            list,
            getString(R.string.net_carbohydrates)
        )
        Constants.SUGAR_INDEX =
            findIndexOfNutrient(list, getString(R.string.sugar))

        Constants.SATURATED_FAT_INDEX = findIndexOfNutrient(
            list,
            getString(R.string.saturated_fat)
        )
        //INIT NUTRITION
        if (Constants.FIBER_INDEX != null) {
            responseForFiber = Triple(
                0f,
                list[Constants.FIBER_INDEX!!].percentOfDailyNeeds!!.toFloat(),
                list[Constants.FIBER_INDEX!!].amount!!.toFloat()
            )
        }

        if (Constants.NET_CARBOHYDRATES_INDEX != null) {
            responseForNetCarbohydrates = Triple(
                0f,
                list[Constants.NET_CARBOHYDRATES_INDEX!!].percentOfDailyNeeds!!.toFloat(),
                list[Constants.NET_CARBOHYDRATES_INDEX!!].amount!!.toFloat()
            )
        }
        if (Constants.SUGAR_INDEX != null) {
            responseForSugar = Triple(
                0f,
                list[Constants.SUGAR_INDEX!!].percentOfDailyNeeds!!.toFloat(),
                list[Constants.SUGAR_INDEX!!].amount!!.toFloat()
            )
        }
        if (Constants.SATURATED_FAT_INDEX != null) {
            responseForSaturatedFat = Triple(
                0f,
                list[Constants.SATURATED_FAT_INDEX!!].percentOfDailyNeeds!!.toFloat(),
                list[Constants.SATURATED_FAT_INDEX!!].amount!!.toFloat()
            )
        }
    }

    private fun initVitaminsIndexAndValues(list: List<ResponseNutrition.Nutrient>) {
        Constants.VitaminB1_INDEX =
            findIndexOfNutrient(
                list,
                getString(R.string.vitamin_b1)
            )

        Constants.VitaminC_INDEX = findIndexOfNutrient(
            list,
            getString(R.string.vitamin_c)
        )
        Constants.VitaminE_INDEX =
            findIndexOfNutrient(list, getString(R.string.vitamin_e))

        Constants.VitaminB3_INDEX = findIndexOfNutrient(
            list,
            getString(R.string.vitaminB3)
        )

        //INIT VITAMINS
        if (Constants.VitaminB3_INDEX != null) {
            responseForVitaminB3 = Triple(
                0f,
                list[Constants.VitaminB3_INDEX!!].percentOfDailyNeeds!!.toFloat(),
                list[Constants.VitaminB3_INDEX!!].amount!!.toFloat(),
            )

        }
        if (Constants.VitaminE_INDEX != null) {
            responseForVitaminE = Triple(
                0f,
                list[Constants.VitaminE_INDEX!!].percentOfDailyNeeds!!.toFloat(),
                list[Constants.VitaminE_INDEX!!].amount!!.toFloat()
            )
        }

        if (Constants.VitaminC_INDEX != null) {
            responseForVitaminC = Triple(
                0f,

                list[Constants.VitaminC_INDEX!!].percentOfDailyNeeds!!.toFloat(),

                list[Constants.VitaminC_INDEX!!].amount!!.toFloat(),
            )
        }



        if (Constants.VitaminB1_INDEX != null) {
            responseForVitaminB = Triple(
                0f,

                list[Constants.VitaminB1_INDEX!!].percentOfDailyNeeds!!.toFloat(),

                list[Constants.VitaminB1_INDEX!!].amount!!.toFloat(),
            )
        }
    }

    private fun findIndexOfNutrient(
        nutrients: List<ResponseNutrition.Nutrient>,
        nutrientName: String
    ): Int? {
        for ((index, nutrient) in nutrients.withIndex()) {
            if (nutrient.name.equals(nutrientName, ignoreCase = true)) {
                return index
            }
        }
        // If the nutrient is not found, return a default value (e.g., -1)
        return null
    }

    private fun setupExpandableDailyNutrition() {

        binding.layoutExpandableNutrition.expandableNutrition.apply {

            initProgressViews()
            setupPickerView()
            parentLayout.setOnClickListener {
                //expandable views has a expanded Listener. so you can use that instead of this
                expanded.postValue(isExpanded)
                if (isExpanded) {
                    collapse()

                } else {
                    expand(ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
        }
    }

    private fun setupPickerView() {
        binding.layoutExpandableNutrition.expandableNutrition.apply {
            val picker = secondLayout.findViewById<ValuePickerView>(R.id.valuePickerView)
            picker.items = getPickerItems()
            picker.setSelectedItem(getPickerItems()[1], true)

            picker.onItemSelectedListener = ValuePickerView.OnItemSelectedListener {
                if (it.title == Constants.VITAMINS) {
                    setVitaminProgressView()


                } else {
                    setDefaultNutritionProgressView()

                }
            }
        }
    }

    private fun getPickerItems(): List<Item> {
        return mutableListOf<Item>().apply {
            add(PickerItem(1, Constants.VITAMINS))
            add(PickerItem(2, Constants.NUTRITION))


        }
    }

    //show nutrition by default.because progress views only shows data when we change the picker options
    private fun setDefaultNutritionProgressView() {
        fiberVitaminD.apply {
            if (::responseForFiber.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForFiber.first,
                    responseForFiber.second,
                    responseForFiber.third,
                    "${getString(R.string.fiber)} (${responseForFiber.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
        netCarbohydratesVitaminB1.apply {
            if (::responseForNetCarbohydrates.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForNetCarbohydrates.first,
                    responseForNetCarbohydrates.second,
                    responseForNetCarbohydrates.third,
                    "${getString(R.string.net_carbohydrates)} (${responseForNetCarbohydrates.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
        sugarVitaminB3.apply {
            if (::sugarVitaminB3.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForSugar.first,
                    responseForSugar.second,
                    responseForSugar.third,
                    "${getString(R.string.sugar)} (${responseForSugar.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
        saturatedFatVitaminC.apply {
            if (::responseForSaturatedFat.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForSaturatedFat.first,
                    responseForSaturatedFat.second,
                    responseForSaturatedFat.third,
                    "${getString(R.string.saturated_fat)} (${responseForSaturatedFat.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
    }

    private fun initProgressViews() {
        binding.layoutExpandableNutrition.expandableNutrition.apply {
            saturatedFatVitaminC =
                secondLayout.findViewById(R.id.progress_SaturatedFat_VitaminC)
            sugarVitaminB3 =
                secondLayout.findViewById(R.id.progress_Sugar_VitaminB3)
            fiberVitaminD =
                secondLayout.findViewById(R.id.progress_Fiber_VitaminD)
            netCarbohydratesVitaminB1 =
                secondLayout.findViewById(R.id.progress_NetCarbohydrates_VitaminB)

        }
    }

    private fun setVitaminProgressView() {
        saturatedFatVitaminC.apply {
            if (::responseForVitaminC.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForVitaminC.first,
                    responseForVitaminC.second,
                    responseForVitaminC.third,
                    "${getString(R.string.vitamin_c)} (${responseForVitaminC.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
        sugarVitaminB3.apply {
            if (::responseForVitaminB3.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForVitaminB3.first,
                    responseForVitaminB3.second,
                    responseForVitaminB3.third,
                    "${getString(R.string.vitaminB3)} (${responseForVitaminB3.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
        fiberVitaminD.apply {
            if (::responseForVitaminE.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForVitaminE.first,
                    responseForVitaminE.second,
                    responseForVitaminE.third,
                    "${getString(R.string.vitamin_e)} (${responseForVitaminE.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
        netCarbohydratesVitaminB1.apply {
            if (::responseForVitaminB.isInitialized) {
                initializeNutrientProperties(
                    this,
                    responseForVitaminB.first,
                    responseForVitaminB.second,
                    responseForVitaminB.third,
                    "${getString(R.string.vitamin_b1)} (${responseForVitaminB.third})"
                )
            } else {
                initializeNutrientProperties(
                    this,
                    0f,
                    10f,
                    0f,
                    getString(R.string.no_data)
                )
            }
        }
    }

    private fun setPlaceholderImage() {
        binding.imgPoster.apply {
            visible()
            load(R.drawable.no_content_found)
        }
    }

    //set min max and progress value to progress view
    private fun initializeNutrientProperties(
        progressView: ProgressView,
        minFromApi: Float,
        maxFromApi: Float,
        progressFromApi: Float,
        labelText: String
    ) {
        progressView.min = minFromApi
        progressView.max = maxFromApi
        progressView.progress = progressFromApi
        progressView.labelText = labelText
    }


    override fun onDestroyView() {
        binding.apply {
            youtubePlayer.release()
            scrollView.viewTreeObserver.removeOnScrollChangedListener(onScrollChangedListener)
        }
        super.onDestroyView()
    }


}