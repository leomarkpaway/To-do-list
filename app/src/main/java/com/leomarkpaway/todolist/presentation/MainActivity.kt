package com.leomarkpaway.todolist.presentation

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseActivity
import com.leomarkpaway.todolist.common.enum.ArgKey.ADD_TODO
import com.leomarkpaway.todolist.common.enum.ArgKey.UPDATE_TODO
import com.leomarkpaway.todolist.common.enum.ArgKey.VIEW_TODO
import com.leomarkpaway.todolist.common.enum.Pattern
import com.leomarkpaway.todolist.common.enum.Pattern.TIME
import com.leomarkpaway.todolist.common.util.convertMillis
import com.leomarkpaway.todolist.common.util.getCurrentDate
import com.leomarkpaway.todolist.common.util.getCurrentWeek
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.ActivityMainBinding
import com.leomarkpaway.todolist.presentation.adapter.CurrentWeekAdapter
import com.leomarkpaway.todolist.presentation.adapter.TodoAdapter
import com.leomarkpaway.todolist.presentation.dialog.DeleteTodoDialogFragment
import com.leomarkpaway.todolist.presentation.dialog.ViewAddUpdateTodoDialogFragment
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
    private lateinit var todoAdapter: TodoAdapter

    override fun initViews() {
        setupCurrentWeek()
        setupSearchBar()
        onClickAddTodo()
    }

    override fun subscribe() {
        observeAllTodo()
    }

    private fun observeAllTodo() {
        lifecycleScope.launch {
            viewModel.getAllTodo().observe(this@MainActivity) { allTodoList ->
                setupTodayTaskList(allTodoList)
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

    private fun setupTodayTaskList(itemList: List<Todo>) = with(binding.rvTodo) {
        val calendar: Calendar = Calendar.getInstance()
        val filterByCurrentDate = itemList.filter {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.dateMillis.convertMillis(calendar, Pattern.DATE.id) == getCurrentDate()
            } else { TODO("VERSION.SDK_INT < O") }
        }
        val allTodoArray = filterByCurrentDate as ArrayList<Todo>
        val sortedByTime = sortByTime(allTodoArray)

        todoAdapter = TodoAdapter(sortedByTime, { onCLickItem(it) }, { onDeleteItem(it) }, { onUpdateItem(it) }, { onMarkAsDone(it) })
        adapter = todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

    }

    private fun setupCurrentWeek() = with(binding.rvWeek) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val week = getCurrentWeek()
            adapter = CurrentWeekAdapter(week)
        } else {
            visibility = View.GONE
        }
        layoutManager = GridLayoutManager(this@MainActivity, 7)
        isLayoutFrozen = true
    }

    private fun setupSearchBar() = with(binding.searchBar) {
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { todoAdapter.filter.filter(s.toString()) }
        })
    }

    private fun onClickAddTodo() = with(binding.faAddTodo) {
        setOnClickListener {
            ViewAddUpdateTodoDialogFragment.newInstance(context = context, methodId = ADD_TODO.id)
                .show(supportFragmentManager, "dialog_add_todo")
        }
    }

    private fun onCLickItem(todo: Todo) {
        ViewAddUpdateTodoDialogFragment.newInstance(context = this, methodId = VIEW_TODO.id)
            .show(supportFragmentManager, "dialog_View_detail_todo")
        viewModel.updateSelectedItem(todo)
    }

    private fun onDeleteItem(todo: Todo) {
        DeleteTodoDialogFragment().show(supportFragmentManager, "dialog_delete_todo")
        viewModel.updateSelectedItem(todo)
    }

    private fun onUpdateItem(todo: Todo) {
        ViewAddUpdateTodoDialogFragment.newInstance(context = this ,methodId = UPDATE_TODO.id)
            .show(supportFragmentManager, "dialog_delete_todo")
        viewModel.updateSelectedItem(todo)
    }

    private fun onMarkAsDone(todo: Todo) { viewModel.updateMarkAsDone(todo) }

}