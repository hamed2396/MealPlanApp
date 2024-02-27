package com.example.mealplan.adapters.meal_fragment

import Restaurant
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.example.mealplan.R
import com.example.mealplan.databinding.ItemRestaurantBinding
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.loadImage
import javax.inject.Inject

class RestaurantAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    private var items = emptyList<Restaurant>()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context=parent.context
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()


    inner class ViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurant) {
            binding.apply {
                item.logoPhotos?.let { logo ->
                    logo.isNotNullOrEmpty.ifTrue { imgLogo.loadImage(logo[0]!!) }
                }
                item.localHours?.operational?.let { openHour ->
                    openHour.monday.isNotNullOrEmpty().ifTrue {

                        txtOpenHour.text = openHour.monday!!
                    }
                }
                item.aggregatedRatingCount?.let {
                    txtRating.text = it.toString()
                    it.logError()
                }
                item.name.isNotNullOrEmpty().ifTrue {
                    txtRestaurantName.text = item.name
                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

            }
        }
    }

    private var onItemClickListener: ((Restaurant) -> Unit)? = null
    fun setOnItemClickListener(listener: (Restaurant) -> Unit) {
        onItemClickListener = listener
    }
    fun setData(data: List<Restaurant>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        result.dispatchUpdatesTo(this)
    }
}