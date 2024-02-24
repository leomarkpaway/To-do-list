package com.leomarkpaway.todolist.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leomarkpaway.todolist.common.util.createRoomDataBase
import com.leomarkpaway.todolist.data.source.local.dao.TodoDao
import com.leomarkpaway.todolist.data.source.local.database.AppDatabase.Companion.VERSION_NUMBER
import com.leomarkpaway.todolist.data.source.local.entity.Todo

@Database(
    entities = [Todo::class],
    version = VERSION_NUMBER,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        const val VERSION_NUMBER = 1

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = context.createRoomDataBase<AppDatabase>("database_name")
                INSTANCE = instance
                instance
            }
        }
    }
}