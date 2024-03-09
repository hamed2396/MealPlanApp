package com.example.mealplan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.databinding.ItemMealTimeBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.base.BaseDiffUtil
import com.example.mealplan.utils.extensions.loadImage
import javax.inject.Inject

class DailyAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    private lateinit var binding: ItemMealTimeBinding
    private var items = emptyList<DailyMealEntity>()

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
        fun bind(item: DailyMealEntity) {
            binding.apply {

                coverImg.loadImage(item.image!!)
                nameTxt.text = item.title

                imgDelete.setOnClickListener {
                    onItemClickListener?.let {
                        it(item,true)
                    }

                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item,false)
                    }
                }
            }


        }
    }

    private var onItemClickListener: ((DailyMealEntity,delete:Boolean) -> Unit)? = null
    fun setOnItemClickListener(listener: (DailyMealEntity,delete:Boolean) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<DailyMealEntity>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val diffUtils = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}