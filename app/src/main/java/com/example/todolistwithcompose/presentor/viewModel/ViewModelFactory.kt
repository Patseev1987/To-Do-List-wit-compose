package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (private val context: Context,private val taskId:Long? = null): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(context = context) as T
        }
        if (modelClass.isAssignableFrom(ViewModelMainScreen::class.java)) {
            return ViewModelMainScreen(context) as T
        }
        if( modelClass.isAssignableFrom(ShowTaskViewModel::class.java)){
            val id = taskId?: throw IllegalArgumentException("taskId wasn't found")
            return ShowTaskViewModel(taskId = id, context = context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}