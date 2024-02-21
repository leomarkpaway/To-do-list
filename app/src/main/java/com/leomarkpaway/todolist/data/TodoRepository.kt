package com.leomarkpaway.todolist.data

import androidx.lifecycle.LiveData
import com.leomarkpaway.todolist.source.local.dao.TodoDao
import com.leomarkpaway.todolist.source.local.entity.Todo

class TodoRepository(private val todoDao: TodoDao) {

    fun getTodos() : LiveData<List<Todo>> = todoDao.getAllData()

     fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }
}