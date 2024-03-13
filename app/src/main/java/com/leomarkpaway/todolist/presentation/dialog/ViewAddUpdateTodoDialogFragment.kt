package com.leomarkpaway.todolist.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.TodoApp
import com.leomarkpaway.todolist.common.base.BaseDialogFragment
import com.leomarkpaway.todolist.common.enum.ArgKey.ADD_TODO
import com.leomarkpaway.todolist.common.enum.ArgKey.VIEW_TODO
import com.leomarkpaway.todolist.common.enum.ArgKey.UPDATE_TODO
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

class ViewAddUpdateTodoDialogFragment(private val context: Context) : BaseDialogFragment<TodoViewModel, DialogFragmentAddTodoBinding>() {
    override val viewModel: TodoViewModel by activityViewModels {
        viewModelFactory { TodoViewModel(TodoApp.appModule.todoRepository) }
    }
    override val layout: Int = R.layout.dialog_fragment_add_todo

    private lateinit var title: InputValidator
    private lateinit var timeDate: InputValidator
    private lateinit var dateTimePicker: DateTimePicker
    private var selectedItem = Todo(null, "", "", "", 0)
    private var disableDatePicker = true

    override fun initViews() {
        super.initViews()
        setupDialogFunction()
        setupDateTimePicker()
        setupSubmit()
        setupCancel()
    }



    override fun subscribe() {
        super.subscribe()
        viewModel.selectedTodo.observe(this) { item ->
            selectedItem = item
            if (item != null && args!!.getBoolean(VIEW_TODO.id) || args!!.getBoolean(UPDATE_TODO.id))
                setupDialogContent(item)
        }
        viewModel.isDisableDatePicker.observe(this) { isDisableDatePicker ->
            disableDatePicker = isDisableDatePicker
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

    private fun setupDialogFunction() = with(binding) {

        if (args!!.getBoolean(VIEW_TODO.id)) {
            tvTitle.text = getString(R.string.dialog_title_view_todo)
            btnSubmit.visibility = View.GONE
            edtTitle.isFocusable = false
            edtDescription.isFocusable = false
            tvLabelDescription.visibility = View.VISIBLE
            viewModel.updateIsDisableDatePicker(true)

            val hintDescription = edtDescription.hint
            val hintDescriptionString = context.getString(R.string.hint_description)
            if (hintDescription == hintDescriptionString) edtDescription.hint = getString(R.string.no_description)
            btnCancel.text = getString(R.string.close)
            btnCancel.background.setTint(ContextCompat.getColor(requireContext(), R.color.alert))
        }

        if (args!!.getBoolean(ADD_TODO.id)) {
            tvTitle.text = getString(R.string.dialog_title_add_todo)
            btnSubmit.text = getText(R.string.btn_submit)
            viewModel.updateIsDisableDatePicker(false)
        }

        if (args!!.getBoolean(UPDATE_TODO.id)) {
            tvTitle.text = getString(R.string.dialog_title_update_todo)
            btnSubmit.text = getText(R.string.btn_update)
            btnSubmit.background.setTint(ContextCompat.getColor(requireContext(), R.color.dark_cyan))
            viewModel.updateIsDisableDatePicker(false)
        }
    }

    private fun setupDateTimePicker() = with(binding.edtDateTime) {
        dateTimePicker = DateTimePicker.newInstance(requireContext())
        setOnClickListener { if (!disableDatePicker) dateTimePicker.showDateTimePicker(it) }
    }

    private fun setupSubmit() = with(binding) {
        title = InputValidator(getString(R.string.input_required), iplTitle,).apply { setListener(edtTitle) }
        timeDate = InputValidator(getString(R.string.input_required), iplDateTime,).apply { setListener(edtDateTime) }

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

    private fun setupCancel() = with(binding) {
        btnCancel.setOnClickListener { dismiss() }
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

    companion object {
        fun newInstance(context: Context, methodId: String) : ViewAddUpdateTodoDialogFragment {
            val fragment = ViewAddUpdateTodoDialogFragment(context)
            val args = Bundle()
            when(methodId) {
                ADD_TODO.id -> { args.putBoolean(ADD_TODO.id, true) }
                VIEW_TODO.id -> { args.putBoolean(VIEW_TODO.id, true) }
                UPDATE_TODO.id -> { args.putBoolean(UPDATE_TODO.id, true) }
            }
            fragment.arguments = args
            return fragment
        }
    }

}