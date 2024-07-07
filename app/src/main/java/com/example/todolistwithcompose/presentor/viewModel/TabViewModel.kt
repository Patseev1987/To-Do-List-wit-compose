package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.TabState
import com.example.todolistwithcompose.utils.toTabItem
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


class TabViewModel @Inject constructor (
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {

    private val _state: MutableStateFlow<TabState> = MutableStateFlow(TabState.Init)
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getTabItems().collect{

            }
        }
    }

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
                return@map if (filter != null) tasks.filter { it.taskGroup == filter } else tasks
            }.map { tasks -> tasks.map { it.toTask() } }
                .map {
                    it.sortedWith(compareBy<Task>{ task -> task.status }
                    .thenBy(nullsLast()) { task -> task.date })
                }
                .collect { tasks -> _state.value = TabState.Result(tasks) }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTaskById(task.id)
        }
    }

}