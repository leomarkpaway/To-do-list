package com.leomarkpaway.todolist

import androidx.activity.viewModels
import com.leomarkpaway.todolist.common.base.BaseActivity
import com.leomarkpaway.todolist.databinding.ActivityMainBinding

class MainActivity : BaseActivity<TodoListViewModel, ActivityMainBinding>() {
    override val viewModel: TodoListViewModel by viewModels()
    override fun inflateBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupViews() {
    }

    override fun subscribe() {
    }
}