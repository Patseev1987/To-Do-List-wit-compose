package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.state.ShowTaskState
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ShowTaskViewModel @Inject constructor(
    private val taskId: Long,
    private val appContext: Application,
    private val dao:Dao
):ViewModel() {


    val state = dao.getTaskById(taskId)
        .map { taskEntity -> taskEntity?.toTask() ?: throw Exception(appContext.getString(R.string.task_not_found)) }
        .map { task -> ShowTaskState.Result(task) as ShowTaskState }
        .stateIn(viewModelScope, SharingStarted.Lazily,ShowTaskState.Loading)

}