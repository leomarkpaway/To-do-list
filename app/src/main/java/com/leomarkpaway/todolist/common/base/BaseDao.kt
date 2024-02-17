package com.leomarkpaway.todolist.common.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface BaseDao<T> {
    @Query("SELECT * FROM todo_list")
    fun getAllData(): List<T>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(data: T)

    @Delete
    fun delete(vararg data: T)
}