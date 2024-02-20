package com.leomarkpaway.todolist.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.leomarkpaway.todolist.common.util.createRoomDataBase
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

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = context.createRoomDataBase<AppDatabase>("todoDataBase")
                INSTANCE = instance
                return instance
            }
        }
    }
}