package com.leomarkpaway.todolist.data.repository

import androidx.lifecycle.LiveData
import com.leomarkpaway.todolist.data.source.local.database.AppDatabase
import com.leomarkpaway.todolist.data.source.local.entity.Todo

interface TodoRepository {
    suspend fun addTodo(todo: Todo)
    suspend fun getAllTodo() : LiveData<List<Todo>>
}
class TodoRepositoryImpl(private val appDatabase: AppDatabase): TodoRepository {
    override suspend fun addTodo(todo: Todo) {
        appDatabase.todoDao().insert(todo)
    }

    override suspend fun getAllTodo(): LiveData<List<Todo>> {
       return appDatabase.todoDao().getAllData()
    }
}