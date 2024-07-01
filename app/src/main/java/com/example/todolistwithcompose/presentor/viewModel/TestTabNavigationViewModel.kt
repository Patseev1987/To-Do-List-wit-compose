package com.example.todolistwithcompose.presentor.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.presentor.state.TestNavigationTabState
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class TestTabNavigationViewModel(private val appContext: Context) : ViewModel() {
    private val dao = TasksDatabase.getInstance(appContext).taskDao

    private val _state: MutableStateFlow<TestNavigationTabState> = MutableStateFlow(TestNavigationTabState.Init)
    val state = _state.asStateFlow()


    fun getTasks(page: Int = 0) {
        when (page) {
            1 -> {
                val filter = TaskGroup.WORK_TASK
                loadData(filter)
            }

            2 -> {
                val filter = TaskGroup.HOME_TASK
                loadData(filter)
            }
            3 -> {
                val filter = TaskGroup.FAMILY_TASK
                loadData(filter)
            }
            else -> {
                val filter = null
                loadData(filter)
            }
        }
    }

    private fun loadData(filter: TaskGroup?) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getTask().map { tasks ->
                return@map tasks.filter { it.taskGroup == filter }
            }.map { tasks -> tasks.map { it.toTask() } }
                .collect { tasks -> _state.value = TestNavigationTabState.Result(tasks) }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTaskById(task.id)
        }
    }

}