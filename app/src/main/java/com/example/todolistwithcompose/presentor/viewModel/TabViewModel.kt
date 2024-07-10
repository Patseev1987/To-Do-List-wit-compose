package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
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
import kotlinx.coroutines.flow.filter
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
        loadData()
    }



    fun loadData(
        tab: TabItem =
    ) {
    viewModelScope.launch(Dispatchers.IO) {
        val tabs = dao.getTabItems().map { it.toTabItem() }
        dao.getTask().map { entity -> entity.map { it.toTask() } }
            .collect{tasks ->
                val tasksList = tasks.map {task ->
                    if (task.tabItemName != ALL_TASKS.name) tasks.filter { tab.name == task.tabItemName } else task
                }
                _state.value = TabState.Result(tasksList, tabs)
            }
    }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTaskById(task.id)
        }
    }

    fun setSelected(tab: TabItem) {

        dao.
    }

   private companion object{
        val ALL_TASKS = TabItem(
            name = "All",
            selectedIcon = Icons.Filled.AutoAwesomeMotion,
            unselectedIcon = Icons.Outlined.AutoAwesomeMotion,
            isSelected = true
        )
    }

}