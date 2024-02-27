package com.leomarkpaway.todolist.presentation.dialog

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseDialogFragment
import com.leomarkpaway.todolist.common.enum.Pattern
import com.leomarkpaway.todolist.common.util.convertMillis
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.DialogFragmentDeleteTodoBinding
import com.leomarkpaway.todolist.presentation.TodoViewModel
import java.util.Calendar

class DeleteTodoDialogFragment : BaseDialogFragment<TodoViewModel, DialogFragmentDeleteTodoBinding>() {
    override val viewModel: TodoViewModel by activityViewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layout: Int = R.layout.dialog_fragment_delete_todo

    override fun initViews() {
        super.initViews()
        Log.d("qwe", "oncreate")
        onClickDelete()
        onCLickCancel()
    }

    override fun subscribe() {
        super.subscribe()
        observeSelectedItem()
    }

    private fun observeSelectedItem() {
        viewModel.selectedTodo.observe(this) { item ->
            Log.d("qwe", "sletd $item")
            if (item != null) setupDialogContent(item)
        }
    }

    private fun setupDialogContent(todo: Todo) = with(binding.tvSelectedItem) {
        val calendar: Calendar = Calendar.getInstance()
        val time = todo.dateTime.convertMillis(calendar, Pattern.TIME.id)
        val date = todo.dateTime.convertMillis(calendar, Pattern.DATE.id)
        text = getString(R.string.selected_item_to_delete, todo.title, time, date)
    }

    private fun onClickDelete() = with(binding.btnDelete) {
        setOnClickListener { viewModel.deleteTodo() }
        dismiss()
    }

    private fun onCLickCancel() = with(binding.btnCancel) {
        setOnClickListener { dismiss() }
    }

}