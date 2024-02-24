package com.leomarkpaway.todolist.presentation.dialog

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseDialogFragment
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.DialogFragmentAddTodoBinding
import com.leomarkpaway.todolist.presentation.TodoViewModel

class DialogFragmentAddTodo : BaseDialogFragment<TodoViewModel, DialogFragmentAddTodoBinding>() {
    override val viewModel: TodoViewModel by activityViewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layout: Int = R.layout.dialog_fragment_add_todo

    override fun initViews() {
        super.initViews()
        onClickSubmit()
        onCLickCancel()
    }

    override fun subscribe() {
        super.subscribe()
    }

    private fun onClickSubmit() = with(binding) {
        val title = tiTitle.editText?.text
        val description = edtDescription.text
        btnSubmit.setOnClickListener {
            viewModel.addTodo(Todo(null,title.toString(), description.toString(), "8:00 pm"))
            dismiss()
        }
    }

    private fun onCLickCancel() = with(binding) {
        btnCancel.setOnClickListener { dismiss() }
    }

}