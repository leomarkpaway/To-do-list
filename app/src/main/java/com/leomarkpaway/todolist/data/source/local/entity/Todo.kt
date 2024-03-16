package com.leomarkpaway.todolist.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date_millis") val dateMillis: Long,
    @ColumnInfo(name = "is_done") val isDone: Boolean? = false,
)