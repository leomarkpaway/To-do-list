package com.leomarkpaway.todolist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.domain.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.addTodo(todo)
        }
    }

}