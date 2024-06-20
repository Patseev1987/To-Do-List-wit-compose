package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory ( val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(context = context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}