package com.leomarkpaway.todolist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leomarkpaway.todolist.databinding.ItemTodoBinding
import java.util.Locale

class TodoAdapter(
    private val itemList: ArrayList<TodoModel>,
    private val onItemClicked: (TodoModel) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoHolder>(), Filterable {

    private val itemListHolder = ArrayList<TodoModel>(itemList)

    inner class TodoHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(item: TodoModel) = with(binding){
           tvTime.text = item.dateTime
           tvTitle.text = item.title.capitalize(Locale.getDefault())
           root.setOnClickListener { onItemClicked(item) }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val binding = DataBindingUtil.inflate<ItemTodoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_todo,
            parent,
            false
        )
        return TodoHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val item = itemListHolder[position]
        holder.bind(item)
    }

    override fun getItemCount() = itemList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                val filteredList = mutableListOf<TodoModel>()
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
                itemList.addAll(results.values as ArrayList<TodoModel>)
                notifyDataSetChanged()
            }
        }
    }

}