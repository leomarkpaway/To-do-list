package com.leomarkpaway.todolist.di

import android.content.Context
import com.leomarkpaway.todolist.data.repository.TodoRepositoryImpl
import com.leomarkpaway.todolist.data.source.local.database.AppDatabase
import com.leomarkpaway.todolist.domain.TodoRepository

interface AppModule {
    val appDatabase: AppDatabase
    val todoRepository: TodoRepository
}
class AppModuleImpl(private val appContext: Context) : AppModule {

    override val appDatabase: AppDatabase = AppDatabase.getDatabase(appContext)
    override val todoRepository: TodoRepository = TodoRepositoryImpl(appDatabase)

}