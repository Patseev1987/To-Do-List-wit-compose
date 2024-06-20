package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory ( val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(application = application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}