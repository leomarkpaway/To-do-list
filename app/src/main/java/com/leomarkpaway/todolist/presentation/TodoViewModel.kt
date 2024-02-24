package com.leomarkpaway.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.domain.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    private val _allTodo = MutableLiveData<List<Todo>>()
    val allTodo: LiveData<List<Todo>> = _allTodo

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.addTodo(todo)
        }
    }

    suspend fun getAllTodo() = todoRepository.getAllTodo()

}