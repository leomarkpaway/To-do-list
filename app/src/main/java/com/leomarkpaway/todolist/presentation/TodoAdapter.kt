package com.leomarkpaway.todolist.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.common.enum.Pattern
import com.leomarkpaway.todolist.common.util.convertMillis
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.ItemTodoBinding
import java.util.Calendar
import java.util.Locale


class TodoAdapter(
    private val itemList: ArrayList<Todo>,
    private val onItemClicked: (Todo) -> Unit,
    private val onItemDelete: (Todo) -> Unit,
    private val onItemUpdate: (Todo) -> Unit,
    private val onClickDone: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoHolder>(), Filterable {

    private val itemListHolder = ArrayList<Todo>(itemList)
    private lateinit var context: Context

    inner class TodoHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
       @SuppressLint("NotifyDataSetChanged")
       fun bind(item: Todo) = with(binding) {
           val calendar: Calendar = Calendar.getInstance()
           tvTime.text = "${item.dateMillis.convertMillis(calendar, Pattern.TIME.id)} - ${item.dateMillis.convertMillis(calendar, Pattern.DATE.id)
               .lowercase(Locale.getDefault())}"
               .lowercase(Locale.getDefault())
           tvTitle.text = item.title.capitalize(Locale.getDefault())
           root.setOnClickListener { onItemClicked(item) }
           imgDelete.setOnClickListener {
               onItemDelete(item)
               notifyDataSetChanged()
           }
           imgEdit.setOnClickListener {
               if (item.id != null) onItemUpdate(item)
               notifyDataSetChanged()
           }
           checkBox.setOnClickListener {
               onClickDone(item)
               checkBox.isChecked = true
               notifyDataSetChanged()
           }

           if (item.status == false) {
               checkBox.isChecked = false
           } else {
               tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
               imgEdit.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit_disable))
               imgDelete.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_disable))
               checkBox.isChecked = true
               imgEdit.isClickable = false
               imgDelete.isClickable = false
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        context = parent.context
        val binding = DataBindingUtil.inflate<ItemTodoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_todo,
            parent,
            false
        )
        return TodoHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount() = itemList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                val filteredList = mutableListOf<Todo>()
                val results = FilterResults()
                if (constraint.isEmpty()) {
                    filteredList.addAll(itemListHolder)
                } else {
                    for (item in itemListHolder) {
                        if (item.title.contains(filterPattern, true)) filteredList.add(item)
                    }
                }
                results.values = filteredList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                itemList.clear()
                @Suppress("UNCHECKED_CAST")
                itemList.addAll(results.values as ArrayList<Todo>)
                notifyDataSetChanged()
            }
        }
    }

}