package com.leomarkpaway.todolist.presentation

data class TodoModel(
    val id: Long = 0,
    val title: String,
    val description: String,
    val dateTime: String,
)
