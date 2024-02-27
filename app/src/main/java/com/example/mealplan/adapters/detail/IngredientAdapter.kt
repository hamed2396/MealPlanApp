package com.example.mealplan.adapters.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.root.logError
import com.example.mealplan.data.models.detail.ResponseDetailInfo
import com.example.mealplan.databinding.ItemIngredientBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.loadImage
import javax.inject.Inject

class IngredientAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    private lateinit var binding: ItemIngredientBinding
    private var items = emptyList<ResponseDetailInfo.ExtendedIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseDetailInfo.ExtendedIngredient) {
            binding.apply {

                item.image?.let {
                    coverImg.loadImage("${Constants.BASE_URL_IMAGES}${Constants.SIZE_IMAGES}${item.image}")

                }
                item.name?.let {
                    nameTxt.text = it
                }
            }
        }
    }

    fun setData(data: List<ResponseDetailInfo.ExtendedIngredient>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)

        val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}