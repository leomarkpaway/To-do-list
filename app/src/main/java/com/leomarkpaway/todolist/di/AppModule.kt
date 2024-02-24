package com.leomarkpaway.todolist.di

import android.content.Context
import com.leomarkpaway.todolist.data.source.local.dao.TodoDao
import com.leomarkpaway.todolist.data.source.local.database.AppDatabase
import com.leomarkpaway.todolist.data.repository.TodoRepository
import com.leomarkpaway.todolist.data.repository.TodoRepositoryImpl

interface AppModule {
    val appDatabase: AppDatabase
    val todoListDao: TodoDao
    val todoRepository: TodoRepository
}
class AppModuleImpl(private val appContext: Context) : AppModule {

    override val appDatabase: AppDatabase = AppDatabase.getDatabase(appContext)
    override val todoListDao: TodoDao = appDatabase.todoDao()
    override val todoRepository: TodoRepository = TodoRepositoryImpl(appDatabase)

}