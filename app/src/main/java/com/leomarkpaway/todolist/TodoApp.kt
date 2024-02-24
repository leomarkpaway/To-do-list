package com.leomarkpaway.todolist

import android.app.Application
import com.leomarkpaway.todolist.di.AppModule
import com.leomarkpaway.todolist.di.AppModuleImpl

class TodoApp : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}