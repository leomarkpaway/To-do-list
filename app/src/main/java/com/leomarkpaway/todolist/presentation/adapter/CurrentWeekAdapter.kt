package com.leomarkpaway.todolist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.common.util.getDayDate
import com.leomarkpaway.todolist.common.util.toShortDayName
import com.leomarkpaway.todolist.databinding.ItemDayNameBinding

class CurrentWeekAdapter(private val items: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<CurrentWeekAdapter.CurrentWeekHolder>() {

    inner class CurrentWeekHolder(private val binding: ItemDayNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) = with(binding) {
            tvDayDate.text = item.first.getDayDate()
            tvDatName.text = item.second.toShortDayName()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentWeekHolder {
        val binding = DataBindingUtil.inflate<ItemDayNameBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_day_name,
            parent,
            false
        )
        return CurrentWeekHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CurrentWeekHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}