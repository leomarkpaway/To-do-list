package com.leomarkpaway.todolist

import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.leomarkpaway.todolist.common.base.BaseActivity
import com.leomarkpaway.todolist.databinding.ActivityMainBinding

class MainActivity : BaseActivity<TodoListViewModel, ActivityMainBinding>() {
    override val viewModel: TodoListViewModel by viewModels()
    override fun inflateBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupViews() {
        setupTodoList()
    }

    override fun subscribe() {
    }

    private fun setupTodoList() = with(binding.rvTodo) {
        val itemList = arrayListOf<TodoModel>()
        for (item in 1 until 100) {
            itemList.add(TodoModel(item.toLong(), "title $item", "", "8:00 pm"))
        }
        adapter = TodoAdapter(itemList) { Log.d("qwe", "item $it") }
        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    }
}