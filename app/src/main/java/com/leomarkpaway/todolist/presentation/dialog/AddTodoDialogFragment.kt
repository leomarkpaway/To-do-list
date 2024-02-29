package com.leomarkpaway.todolist.presentation.dialog

import androidx.fragment.app.activityViewModels
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseDialogFragment
import com.leomarkpaway.todolist.common.enum.Pattern
import com.leomarkpaway.todolist.common.uielement.DateTimePicker
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.DialogFragmentAddTodoBinding
import com.leomarkpaway.todolist.presentation.TodoViewModel

class AddTodoDialogFragment : BaseDialogFragment<TodoViewModel, DialogFragmentAddTodoBinding>() {
    override val viewModel: TodoViewModel by activityViewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layout: Int = R.layout.dialog_fragment_add_todo

    private lateinit var dateTimePicker: DateTimePicker

    override fun initViews() {
        super.initViews()
        setupDateTimePicker()
        onClickSubmit()
        onCLickCancel()
    }

    override fun subscribe() {
        super.subscribe()
    }

    private fun setupDateTimePicker() = with(binding.edtDateTime) {
        dateTimePicker = DateTimePicker.newInstance(requireContext())
        setOnClickListener { dateTimePicker.showDateTimePicker(it) }
    }

    private fun onClickSubmit() = with(binding) {
        val title = edtTitle.text
        val description = edtDescription.text
        val time = dateTimePicker.getConvertedMillis(Pattern.TIME.id)
        btnSubmit.setOnClickListener {
            viewModel.addTodo(Todo(null,title.toString(), description.toString(), time, dateTimePicker.getMillis()))
            dismiss()
        }
    }

    private fun onCLickCancel() = with(binding) {
        btnCancel.setOnClickListener { dismiss() }
    }

}