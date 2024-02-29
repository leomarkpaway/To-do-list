package com.leomarkpaway.todolist.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.leomarkpaway.todolist.common.base.BaseDao
import com.leomarkpaway.todolist.data.source.local.entity.Todo

@Dao
interface TodoDao: BaseDao<Todo> {
    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun getItemById(id: Int): Todo

    @Query("SELECT * FROM todo_list ORDER BY date_millis DESC")
    fun getAllDataSortByDate(): LiveData<List<Todo>>
    companion object {
        const val TABLE_NAME = "todo_list"
    }
}