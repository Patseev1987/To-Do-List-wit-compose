package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.state.ShowTaskState
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ShowTaskViewModel(private val taskId:Long, private val appContext: Context):ViewModel() {
   private val dao = TasksDatabase.getInstance(context = appContext).taskDao

    val state = dao.getTaskById(taskId)
        .map { taskEntity -> taskEntity?.toTask() ?: throw Exception("Task not found") }
        .map { task -> ShowTaskState.Result(task) as ShowTaskState }
        .stateIn(viewModelScope, SharingStarted.Lazily,ShowTaskState.Loading)

}