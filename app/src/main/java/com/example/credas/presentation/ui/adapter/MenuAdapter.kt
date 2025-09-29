package com.example.credas.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credas.databinding.HeaderItemBinding
import com.example.credas.databinding.RowItemBinding
import com.example.credas.domain.model.Food
import com.example.credas.domain.model.GroupedFood

class FoodAdapter(
    private val onItemClicked: (Food) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ROW = 1
    }

    private var foods: MutableList<GroupedFood> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RowViewHolder(view, onItemClicked)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (foods[position]) {
            is GroupedFood.Category -> TYPE_HEADER
            is GroupedFood.Foods -> TYPE_ROW
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = foods[position]) {
            is GroupedFood.Category -> (holder as HeaderViewHolder).bind(item)
            is GroupedFood.Foods -> (holder as RowViewHolder).bind(item.food)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFoods(items: List<GroupedFood>) {
        foods.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    class HeaderViewHolder(val view: HeaderItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: GroupedFood.Category) {
            view.category = item
        }
    }

    class RowViewHolder(val view: RowItemBinding, val onItemClicked: (Food) -> Unit) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: Food) {
            view.food = item

            view.root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
}