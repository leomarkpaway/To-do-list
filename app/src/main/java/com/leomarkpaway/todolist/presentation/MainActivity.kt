package com.leomarkpaway.todolist.presentation

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseActivity
import com.leomarkpaway.todolist.common.enum.Pattern.TIME
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.ActivityMainBinding
import com.leomarkpaway.todolist.presentation.dialog.AddUpdateTodoDialogFragment
import com.leomarkpaway.todolist.presentation.dialog.DeleteTodoDialogFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
            AddUpdateTodoDialogFragment.newInstance(context = context, isAddTodo = true)
                .show(supportFragmentManager, "dialog_add_todo")
        }
    }

    private fun observeAllTodo() {
        lifecycleScope.launch {
            viewModel.getAllTodo().observe(this@MainActivity) { allTodoList ->
                val allTodoArray = allTodoList as ArrayList<Todo>
                val sortedByTime = sortByTime(allTodoArray)
                setupTodoList(sortedByTime)
            }
        }
    }

    private fun sortByTime(modelList: ArrayList<Todo>): ArrayList<Todo> {
        val timeComparator = Comparator<Todo> { model1, model2 ->
            val format = SimpleDateFormat(TIME.id, Locale.getDefault())
            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()
            calendar1.time = format.parse(model1.time) ?: Date()
            calendar2.time = format.parse(model2.time) ?: Date()
            calendar1.compareTo(calendar2)
        }
        modelList.sortWith(timeComparator)
        return modelList
    }

    private fun setupTodoList(itemList: ArrayList<Todo>) = with(binding.rvTodo) {
        adapter = TodoAdapter(itemList, { onCLickItem(it) }, { onDeleteItem(it) }, { onUpdateItem(it) })
        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

    }

    private fun onCLickItem(item: Todo) {

    }

    private fun onDeleteItem(item: Todo) {
        DeleteTodoDialogFragment().show(supportFragmentManager, "dialog_delete_todo")
        viewModel.updateSelectedItem(item)
    }

    private fun onUpdateItem(todo: Todo) {
        AddUpdateTodoDialogFragment.newInstance(context = this , isAddTodo = false)
            .show(supportFragmentManager, "dialog_delete_todo")
        viewModel.updateSelectedItem(todo)
    }
}