package com.example.mealplan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.gone

import com.example.mealplan.data.models.ResponseWeeklyMeals
import com.example.mealplan.data.models.WeeklyMeal
import com.example.mealplan.databinding.ItemMealTimeBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.loadImage
import javax.inject.Inject
import kotlin.math.roundToInt

class WeeklyAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<WeeklyAdapter.ViewHolder>() {

    private lateinit var binding: ItemMealTimeBinding
    private var items = emptyList<WeeklyMeal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMealTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: WeeklyMeal) {
            binding.apply {

                nameTxt.text = item.title
                coverImg.loadImage("${Constants.BASE_URL_RECIPE_IMAGES}${item.id}-${Constants.RECIPE_SIZE_IMAGES}.jpg")

                imgDelete.gone()
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

            }



        }
    }

    private var onItemClickListener: ((WeeklyMeal) -> Unit)? = null
    fun setOnItemClickListener(listener: (WeeklyMeal) -> Unit) {
        onItemClickListener = listener
    }

    fun setSaturdayData(data: ResponseWeeklyMeals?) {
        if (data == null) return

        val allMeals: MutableList<WeeklyMeal> = mutableListOf()


        data.week?.let { week ->
            week.saturday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.saturday.nutrients?.calories?.roundToInt(),
                        carbohydrates = week.saturday.nutrients?.calories?.roundToInt(),
                        fat = week.saturday.nutrients?.fat?.roundToInt(),
                        protein = week.saturday.nutrients?.protein?.roundToInt()


                    )
                })
            }


            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }

    fun setSundayData(data: ResponseWeeklyMeals?) {
        if (data == null) return
        val allMeals: MutableList<WeeklyMeal> = mutableListOf()
        data.week?.let { week ->

            week.sunday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.sunday.nutrients?.calories?.roundToInt(),
                        carbohydrates = week.sunday.nutrients?.calories?.roundToInt(),
                        fat = week.sunday.nutrients?.fat?.roundToInt(),
                        protein = week.sunday.nutrients?.protein?.roundToInt()


                    )
                })
            }

            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }

    fun setMondayData(data: ResponseWeeklyMeals?) {
        if (data == null) return
        val allMeals: MutableList<WeeklyMeal> = mutableListOf()
        data.week?.let { week ->

            week.monday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.monday.nutrients?.calories?.roundToInt(),
                        carbohydrates = week.monday.nutrients?.calories?.roundToInt(),
                        fat = week.monday.nutrients?.fat?.roundToInt(),
                        protein = week.monday.nutrients?.protein?.roundToInt()


                    )
                })
            }

            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }
    fun setTuesdayData(data: ResponseWeeklyMeals?) {
        if (data == null) return
        val allMeals: MutableList<WeeklyMeal> = mutableListOf()
        data.week?.let { week ->

            week.tuesday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.tuesday.nutrients.calories?.roundToInt(),
                        carbohydrates = week.tuesday.nutrients.calories?.roundToInt(),
                        fat = week.tuesday.nutrients.fat?.roundToInt(),
                        protein = week.tuesday.nutrients.protein?.roundToInt()


                    )
                })
            }

            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }
    fun setWednesdayData(data: ResponseWeeklyMeals?) {
        if (data == null) return
        val allMeals: MutableList<WeeklyMeal> = mutableListOf()
        data.week?.let { week ->

            week.wednesday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.wednesday.nutrients.calories?.roundToInt(),
                        carbohydrates = week.wednesday.nutrients.calories?.roundToInt(),
                        fat = week.wednesday.nutrients.fat?.roundToInt(),
                        protein = week.wednesday.nutrients.protein?.roundToInt()


                    )
                })
            }

            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }
    fun setThursdayData(data: ResponseWeeklyMeals?) {
        if (data == null) return
        val allMeals: MutableList<WeeklyMeal> = mutableListOf()
        data.week?.let { week ->

            week.thursday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.thursday.nutrients?.calories?.roundToInt(),
                        carbohydrates = week.thursday.nutrients?.calories?.roundToInt(),
                        fat = week.thursday.nutrients?.fat?.roundToInt(),
                        protein = week.thursday.nutrients?.protein?.roundToInt()


                    )
                })
            }

            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }
    fun setFridayData(data: ResponseWeeklyMeals?) {
        if (data == null) return
        val allMeals: MutableList<WeeklyMeal> = mutableListOf()
        data.week?.let { week ->

            week.friday.meals.let { meals ->
                // Map each meal to a WeeklyMeal object
                allMeals.addAll(meals.map { meal ->
                    WeeklyMeal(
                        id = meal.id,
                        title = meal.title,
                        calories = week.friday.nutrients.calories?.roundToInt(),
                        carbohydrates = week.friday.nutrients.calories?.roundToInt(),
                        fat = week.friday.nutrients.fat?.roundToInt(),
                        protein = week.friday.nutrients.protein?.roundToInt()


                    )
                })
            }

            baseDiffUtil.setNewList(allMeals)
            baseDiffUtil.setOldList(items)
            val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
            items = allMeals
            diffUtils.dispatchUpdatesTo(this)
        }

    }
}