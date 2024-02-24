package com.leomarkpaway.todolist.presentation

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseActivity
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.ActivityMainBinding
import com.leomarkpaway.todolist.presentation.dialog.DialogFragmentAddTodo
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<TodoViewModel, ActivityMainBinding>() {
    override val viewModel: TodoViewModel by viewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layoutId = R.layout.activity_main

    override fun initViews() {
        onClickAddTodo()
    }

    override fun subscribe() {
        observeAllTodo()
    }

    private fun onClickAddTodo() = with(binding.faAddTodo) {
        setOnClickListener {
            DialogFragmentAddTodo().show(supportFragmentManager, "dialog_add_todo")
        }
    }

    private fun observeAllTodo() {
        lifecycleScope.launch {
            viewModel.getAllTodo().observe(this@MainActivity) { allTodoList ->
                val allTodoArray = allTodoList as ArrayList<Todo>
                setupTodoList(allTodoArray)
            }
        }
    }

    private fun setupTodoList(itemList: ArrayList<Todo>) = with(binding.rvTodo) {
        adapter = TodoAdapter(itemList) { Log.d("qwe", "item $it") }
        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    }
}