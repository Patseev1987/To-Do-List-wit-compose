package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolistwithcompose.presentor.state.AddAndUpdateTaskState

class ViewModelFactory (private val appContext: Context,private val taskId:Long = 0L): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddAndUpdateTaskViewModel::class.java)) {
            return AddAndUpdateTaskViewModel(taskId, appContext) as T
        }
        if(modelClass.isAssignableFrom(TestTabNavigationViewModel::class.java)) {
            return TestTabNavigationViewModel(appContext) as T
        }
        if (modelClass.isAssignableFrom(ViewModelMainScreen::class.java)) {
            return ViewModelMainScreen(appContext) as T
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