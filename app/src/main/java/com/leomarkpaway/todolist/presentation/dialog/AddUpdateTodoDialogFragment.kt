package com.leomarkpaway.todolist.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseDialogFragment
import com.leomarkpaway.todolist.common.enum.ArgKey.ADD_TODO
import com.leomarkpaway.todolist.common.enum.Pattern.DATE
import com.leomarkpaway.todolist.common.enum.Pattern.DAY_NAME
import com.leomarkpaway.todolist.common.enum.Pattern.TIME
import com.leomarkpaway.todolist.common.uielement.DateTimePicker
import com.leomarkpaway.todolist.common.util.InputValidator
import com.leomarkpaway.todolist.common.util.convertMillis
import com.leomarkpaway.todolist.common.util.viewModelFactory
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.databinding.DialogFragmentAddTodoBinding
import com.leomarkpaway.todolist.presentation.TodoViewModel
import java.util.Calendar

class AddUpdateTodoDialogFragment(private val context: Context) : BaseDialogFragment<TodoViewModel, DialogFragmentAddTodoBinding>() {
    override val viewModel: TodoViewModel by activityViewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layout: Int = R.layout.dialog_fragment_add_todo

    private lateinit var title: InputValidator
    private lateinit var dateTimePicker: DateTimePicker
    private var id: Long? = null

    override fun initViews() {
        super.initViews()
        setupDialogTitle()
        setupDateTimePicker()
        setupSubmit()
        setupCancel()
    }

    override fun subscribe() {
        super.subscribe()
        viewModel.selectedTodo.observe(this) { item ->
            id = item.id
            if (item != null && !args!!.getBoolean(ADD_TODO.id))
                setupDialogContent(item)
        }
    }

    private fun setupDialogContent(item: Todo) = with(binding) {
        val calendar: Calendar = Calendar.getInstance()
        val dayName = item.dateMillis.convertMillis(calendar, DAY_NAME.id)
        val time = item.time
        val date = item.dateMillis.convertMillis(calendar, DATE.id)
        val formattedDateTime = context.getString(R.string.input_holder_time_and_date, dayName, time, date)

        edtTitle.setText(item.title)
        edtDateTime.setText(formattedDateTime)
        edtDescription.setText(item.description)
    }

    private fun setupDialogTitle() {
        binding.tvTitle.text = if (args!!.getBoolean(ADD_TODO.id))
            context.getString(R.string.dialog_title_add_todo)
        else context.getString(R.string.dialog_title_update_todo)
    }

    private fun setupDateTimePicker() = with(binding.edtDateTime) {
        dateTimePicker = DateTimePicker.newInstance(requireContext())
        setOnClickListener { dateTimePicker.showDateTimePicker(it) }
    }

    private fun setupSubmit() = with(binding) {
        title = InputValidator(getString(R.string.input_required), iplTitle,).apply { setListener(edtDateTime) }
        btnSubmit.text = if (args!!.getBoolean(ADD_TODO.id)) getText(R.string.btn_submit) else getText(R.string.btn_update)
        val buttonColor = if (args!!.getBoolean(ADD_TODO.id)) ContextCompat.getColor(requireContext(), R.color.light_yellow) else ContextCompat.getColor(requireContext(), R.color.dark_cyan)
        btnSubmit.background.setTint(buttonColor)

        btnSubmit.setOnClickListener {


        }
    }

    private fun validateAndCreateTodo() {
        val description = binding.edtDescription.text
        val time = dateTimePicker.getConvertedMillis(TIME.id)
        val dateMillis = dateTimePicker.getMillis()

        if (args!!.getBoolean(ADD_TODO.id)) {
            if (title.isValid()) { viewModel.addTodo(Todo(null, title.getStringValue(), description.toString(), time, dateMillis))
                dismiss()
            } else {
                binding.iplTitle.helperText = getString(R.string.input_required)
                // TODO show toast for invalid input
                Log.d("qwe", "show toast")
            }
        } else {
            viewModel.updateTodo(Todo(null, title.toString(), description.toString(), time, dateMillis))
            dismiss()
        }
    }

    private fun setupCancel() = with(binding) {
        btnCancel.setOnClickListener { dismiss() }
    }

    companion object {
        fun newInstance(context: Context, isAddTodo: Boolean) : AddUpdateTodoDialogFragment {
            val fragment = AddUpdateTodoDialogFragment(context)
            val args = Bundle()
            args.putBoolean(ADD_TODO.id, isAddTodo)
            fragment.arguments = args
            return fragment
        }
    }

}