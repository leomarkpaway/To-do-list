package com.leomarkpaway.todolist.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewModel, VB : ViewBinding> : AppCompatActivity() {
    abstract val viewModel: T

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding()
        setContentView(binding.root)
        setupViews()
        subscribe()
    }

    abstract fun inflateBinding(): VB

    abstract fun setupViews()

    abstract fun subscribe()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}