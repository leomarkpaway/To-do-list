package com.leomarkpaway.todolist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.domain.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    private val _selectedTodo = MutableLiveData<Todo>()
    val selectedTodo: LiveData<Todo> = _selectedTodo

    fun updateSelectedItem(todo: Todo) {
        viewModelScope.launch {
            _selectedTodo.value = todo
        }
    }
    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.addTodo(todo)
        }
    }

    suspend fun getAllTodo() = todoRepository.getAllTodo()

    fun deleteTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedTodo = _selectedTodo.value
            Log.d("qwe", "selectedTodo $selectedTodo")
            if (selectedTodo != null) todoRepository.deleteTodo(selectedTodo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            val todoCopy = todo.copy(id = _selectedTodo.value?.id)
            todoRepository.updateTodo(todoCopy)
        }
    }

}