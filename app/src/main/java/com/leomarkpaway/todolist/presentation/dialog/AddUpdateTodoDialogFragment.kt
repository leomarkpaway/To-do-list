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
    private lateinit var timeDate: InputValidator
    private lateinit var dateTimePicker: DateTimePicker
    private var selectedItem = Todo(null, "", "", "", 0)

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
            selectedItem = item
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
        title = InputValidator(getString(R.string.input_required), iplTitle,).apply { setListener(edtTitle) }
        timeDate = InputValidator(getString(R.string.input_required), iplDateTime,).apply { setListener(edtDateTime) }

        btnSubmit.text = if (args!!.getBoolean(ADD_TODO.id)) getText(R.string.btn_submit) else getText(R.string.btn_update)
        val buttonColor = if (args!!.getBoolean(ADD_TODO.id)) ContextCompat.getColor(requireContext(), R.color.light_yellow) else ContextCompat.getColor(requireContext(), R.color.dark_cyan)
        btnSubmit.background.setTint(buttonColor)

        btnSubmit.setOnClickListener {
            val isFormValid = title.isValid() && timeDate.isValid()
            binding.iplTitle.helperText = if (title.isValid()) null else getString(R.string.input_required)
            binding.iplDateTime.helperText = if (timeDate.isValid()) null else getString(R.string.input_required)

            val todo = createTodo()
            if (args!!.getBoolean(ADD_TODO.id)) {
                if (isFormValid) {
                    viewModel.addTodo(todo)
                    dismiss()
                }
            } else {
                if (isFormValid) {
                    viewModel.updateTodo(todo)
                    dismiss()
                }
            }
        }
    }

    private fun createTodo() : Todo {
        val currentTimeDateString = binding.edtDateTime.text.toString()
        val oldDateMillis = selectedItem.dateMillis
        val oldDayName = oldDateMillis.convertMillis(dateTimePicker.getCalendar(), DAY_NAME.id)
        val oldTime = selectedItem.time
        val oldDate = oldDateMillis.convertMillis(dateTimePicker.getCalendar(), DATE.id)
        val oldTimeDateString = context.getString(R.string.input_holder_time_and_date, oldDayName,oldTime, oldDate)

        val time: String
        val dateMillis: Long
        val description = binding.edtDescription.text.toString()
        if (oldTimeDateString == currentTimeDateString) {
            time = selectedItem.time
            dateMillis = oldDateMillis
        } else {
            time = dateTimePicker.getConvertedMillis(TIME.id)
            dateMillis = dateTimePicker.getMillis()
        }

        return Todo(null, title.getStringValue(), description, time, dateMillis)
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