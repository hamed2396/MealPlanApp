package com.example.mealplan.adapters.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.gone
import com.example.mealplan.data.models.detail.ResponseSimilar
import com.example.mealplan.databinding.ItemMealTimeBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.loadImage
import javax.inject.Inject

class SimilarAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) : RecyclerView.Adapter<SimilarAdapter.ViewHolder>() {

    private lateinit var binding: ItemMealTimeBinding
    private var items = emptyList<ResponseSimilar.ResponseSimilarItem>()

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
        fun bind(item: ResponseSimilar.ResponseSimilarItem) {
            binding.apply {
                //Text
                nameTxt.text = item.title
                //Image
                val image = "${Constants.BASE_URL_RECIPE_IMAGES}${item.id}-${Constants.RECIPE_SIZE_IMAGES}.jpg"
                coverImg.loadImage(image)
                imgDelete.gone()
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }
                }
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ResponseSimilar.ResponseSimilarItem>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}