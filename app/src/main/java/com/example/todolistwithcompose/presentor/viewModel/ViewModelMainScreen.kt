package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.state.MainScreenState
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ViewModelMainScreen(
    private val appContext: Context
) : ViewModel() {
 private   val dao = TasksDatabase.getInstance(appContext).taskDao
    val state = dao.getTask()
        .map { it.map { taskEntity -> taskEntity.toTask() } }
        .map{ MainScreenState.Result(it) as MainScreenState }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MainScreenState.Loading
        )


    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
                dao.clearTaskById(task.id)
        }
    }

}