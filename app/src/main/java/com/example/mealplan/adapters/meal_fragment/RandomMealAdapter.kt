package com.example.mealplan.adapters.meal_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.ifFalse
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.isNotNull
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.example.mealplan.R
import com.example.mealplan.data.models.meal.ResponseRandomMeal.Result
import com.example.mealplan.data.models.meal.ResponseRandomMeal.Result.AnalyzedInstruction.Step
import com.example.mealplan.databinding.ItemRandomMealBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.loadImage
import com.example.mealplan.utils.extensions.showToolTip
import javax.inject.Inject

class RandomMealAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<RandomMealAdapter.ViewHolder>() {
    private lateinit var context: Context
    private var items = emptyList<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRandomMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.initAnimation()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clearAnimation()
    }

    inner class ViewHolder(private val binding: ItemRandomMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Result) {
            binding.apply {
                if (item.image.isNotNullOrEmpty()) {
                    imgPoster.loadImage(item.image!!)
                } else {
                    imgPoster.load(R.drawable.placeholder)
                }
                item.title.isNotNullOrEmpty().ifTrue {
                    txtTitle.text = item.title!!
                }.ifFalse { txtTitle.text = context.getString(R.string.notIncluded) }

                item.nutrition?.nutrients?.get(0)?.amount?.let { amount ->
                    txtCals.apply {
                        text = "${amount.toInt()} Cals"
                    }
                }
                item.readyInMinutes?.let {
                    txtTime.text = "$it Min"
                }
                item.nutrition?.nutrients?.get(1)?.amount?.let { fat ->
                    txtFat.apply {
                        text = fat.toInt().toString()
                        setOnClickListener {
                            it.showToolTip(Constants.FAT, fat.toInt().toString())
                        }
                    }
                }
                item.nutrition?.nutrients?.get(3)?.amount?.let { carbs ->
                    txtCarbs.apply {
                        text = carbs.toInt().toString()
                        setOnClickListener {
                            it.showToolTip(Constants.CARBS, carbs.toInt().toString())
                        }
                    }
                }
                item.nutrition?.nutrients?.get(8)?.amount?.let { protein ->
                    txtProtein.apply {
                        text = protein.toInt().toString()
                        setOnClickListener {
                            it.showToolTip(Constants.PROTEIN, protein.toInt().toString())
                        }
                    }
                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        val step = item.analyzedInstructions?.get(0)
                        if (step.isNotNull)
                            it(item.id!!, step!!)
                        else it(item.id!!, null)
                    }
                }
            }

        }

        fun initAnimation() {
            binding.root.animation =
                AnimationUtils.loadAnimation(context, R.anim.random_recycler_anim)
        }

        fun clearAnimation() {
            binding.root.clearAnimation()
        }
    }

    private var onItemClickListener: ((Int, Result.AnalyzedInstruction?) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int, Result.AnalyzedInstruction?) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<Result>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        result.dispatchUpdatesTo(this)
    }
}