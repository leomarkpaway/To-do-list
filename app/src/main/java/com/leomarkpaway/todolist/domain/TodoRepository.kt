package com.leomarkpaway.todolist.domain

import androidx.lifecycle.LiveData
import com.leomarkpaway.todolist.data.source.local.entity.Todo

interface TodoRepository {
    suspend fun addTodo(todo: Todo)
    suspend fun getAllTodo() : LiveData<List<Todo>>
    suspend fun deleteTodo(todo: Todo)
}