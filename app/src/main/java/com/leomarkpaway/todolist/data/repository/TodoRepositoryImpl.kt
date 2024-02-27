package com.leomarkpaway.todolist.data.repository

import androidx.lifecycle.LiveData
import com.leomarkpaway.todolist.data.source.local.database.AppDatabase
import com.leomarkpaway.todolist.data.source.local.entity.Todo
import com.leomarkpaway.todolist.domain.TodoRepository


class TodoRepositoryImpl(private val appDatabase: AppDatabase): TodoRepository {
    override suspend fun addTodo(todo: Todo) {
        appDatabase.todoDao().insert(todo)
    }

    override suspend fun getAllTodo(): LiveData<List<Todo>> {
       return appDatabase.todoDao().getAllData()
    }

    override suspend fun deleteTodo(todo: Todo) {
        appDatabase.todoDao().delete(todo)
    }
}