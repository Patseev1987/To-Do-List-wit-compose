package com.example.todolistwithcompose.presentor.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (private val appContext: Context,private val taskId:Long = 0L): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddAndUpdateTaskViewModel::class.java)) {
            return AddAndUpdateTaskViewModel(taskId, appContext) as T
        }
        if(modelClass.isAssignableFrom(TabViewModel::class.java)) {
            return TabViewModel(appContext) as T
        }
        if( modelClass.isAssignableFrom(ShowTaskViewModel::class.java)){
            if (taskId == 0L){
                throw IllegalArgumentException("taskId wasn't found")
            }
            return ShowTaskViewModel(taskId = taskId, appContext = appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}