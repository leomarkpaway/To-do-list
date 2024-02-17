package com.leomarkpaway.todolist.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leomarkpaway.todolist.source.local.dao.TodoLisDao
import com.leomarkpaway.todolist.source.local.database.AppDatabase.Companion.VERSION_NUMBER
import com.leomarkpaway.todolist.source.local.entity.Todo

@Database(
    entities = [Todo::class],
    version = VERSION_NUMBER
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoLisDao(): TodoLisDao

    companion object {
        const val VERSION_NUMBER = 1
    }
}