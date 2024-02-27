package com.example.mealplan.adapters.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplan.data.models.meal.ResponseRandomMeal.Result.AnalyzedInstruction.Step
import com.example.mealplan.databinding.ItemStepBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.minToHour
import javax.inject.Inject

class StepsAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    private lateinit var binding: ItemStepBinding
    private var items = emptyList<Step>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = Constants.STEPS_COUNT

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Step) {
            binding.apply {
                //Text
                stepTxt.text = "${absoluteAdapterPosition + 1}"
                item.length?.let {
                    timeTxt.text = item.length.number!!.minToHour()
                }
                infoTxt.text = item.step
            }
        }
    }

    fun setData(data: List<Step>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}