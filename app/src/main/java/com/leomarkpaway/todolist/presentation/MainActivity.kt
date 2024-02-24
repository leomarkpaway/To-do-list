package com.leomarkpaway.todolist.presentation

import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseActivity
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.databinding.ActivityMainBinding
import com.leomarkpaway.todolist.presentation.dialog.DialogFragmentAddTodo

class MainActivity : BaseActivity<TodoViewModel, ActivityMainBinding>() {
    override val viewModel: TodoViewModel by viewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layoutId = R.layout.activity_main

    override fun initViews() {
        setupTodoList()
        onClickAddTodo()
    }

    override fun subscribe() {
    }

    private fun onClickAddTodo() = with(binding.faAddTodo) {
        setOnClickListener {
            DialogFragmentAddTodo().show(supportFragmentManager, "dialog_add_todo")
        }
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