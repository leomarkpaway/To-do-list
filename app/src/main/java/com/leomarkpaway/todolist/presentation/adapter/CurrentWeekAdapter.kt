package com.leomarkpaway.todolist.presentation.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.common.util.convertDpToPixels
import com.leomarkpaway.todolist.common.util.getCurrentDate
import com.leomarkpaway.todolist.common.util.getDayDate
import com.leomarkpaway.todolist.common.util.toShortDayName
import com.leomarkpaway.todolist.databinding.ItemDayNameBinding

class CurrentWeekAdapter(private val items: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<CurrentWeekAdapter.CurrentWeekHolder>() {

    private lateinit var context: Context

    inner class CurrentWeekHolder(private val binding: ItemDayNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) = with(binding) {
            val currentDate: String
            val itemDate = item.first
            val shortDayName = item.second.toShortDayName()
            tvDayDate.text = itemDate.getDayDate()
            tvDatName.text = shortDayName

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentDate = getCurrentDate()
                if (currentDate == itemDate) {
                    conDate.background.setTint(ContextCompat.getColor(context, R.color.light_yellow))
                    tvDatName.setTextColor(ContextCompat.getColor(context, R.color.light_yellow))
                    tvDatName.textSize = 18F
                    conDate.updateLayoutParams<LinearLayout.LayoutParams> {
                        height = convertDpToPixels(context, 33F)
                        width = convertDpToPixels(context, 33F)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentWeekHolder {
        context = parent.context
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