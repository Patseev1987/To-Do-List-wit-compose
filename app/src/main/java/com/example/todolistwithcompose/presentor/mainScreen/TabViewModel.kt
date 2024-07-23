package com.example.todolistwithcompose.presentor.mainScreen

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context.ALARM_SERVICE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.useCases.*
import com.example.todolistwithcompose.utils.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


class TabViewModel @Inject constructor(
    private val appContext: Application,
    private val getSelectedTabItemUseCase: GetSelectedTabItemUseCase,
    private val getTabItemsUseCase: GetTabItemsUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val insertTabItemUseCase: InsertTabItemUseCase
) : ViewModel() {

    private var cacheTabs = listOf<TabItem>()
    private var cacheTasks = listOf<Task>()
    private var cacheSelectedTab = ALL_TASKS
    private val _state = MutableStateFlow<TabState>(TabState.Init)
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkFirstStart()
            cacheSelectedTab = getSelectedTabItemUseCase()
                ?: throw IllegalArgumentException("Selected TabItem is null")
            getTabItemsUseCase().collect { tabs ->
                cacheTabs = tabs
                _state.value = TabState.Result(
                    tasks = cacheTasks.withFilter(cacheSelectedTab).specialSort(),
                    tabs = cacheTabs
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getTasksUseCase().collect { tasks ->
                cacheTasks = tasks
                _state.value = TabState.Result(
                    tasks = cacheTasks.withFilter(cacheSelectedTab).specialSort(),
                    tabs = cacheTabs
                )
            }
        }
    }


    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            if (task.isRemind) {
                cancelAlarmWhenDeleteTask(task)
            }
            deleteTaskUseCase(task.id)
        }
    }

    fun setSelected(tab: TabItem) {
        viewModelScope.launch {
            val oldSelected = getSelectedTabItemUseCase()
                ?: throw IllegalStateException("Selected tab must be!")
            val unselected = oldSelected.copy(isSelected = false)
            val newSelected = tab.copy(isSelected = true)
            cacheSelectedTab = newSelected
            insertTabItemUseCase(unselected)
            insertTabItemUseCase(newSelected)
        }

    }

    private fun List<Task>.withFilter(tab: TabItem): List<Task> {
        return if (tab.name == ALL_TASKS.name) this else this.filter { it.tabItemName == tab.name }
    }

    private fun List<Task>.specialSort(): List<Task> = this.sortedWith(compareBy<Task> { task -> task.status }
        .thenBy(nullsLast()) { task -> task.date })

    private suspend fun checkFirstStart() {
        val tabs = getTabItemsUseCase().firstOrNull() ?: emptyList()
        if (tabs.isEmpty()) {
            insertTabItemUseCase(ALL_TASKS)
        }
    }


    private fun cancelAlarmWhenDeleteTask(task: Task) {
        val alarmManager = appContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newAlarmIntent(appContext, task.title, task.content)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            task.id.toInt(),
            intent,
            FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        val ALL_TASKS = TabItem(
            name = "All",
            selectedIcon = Icons.Filled.AutoAwesomeMotion,
            unselectedIcon = Icons.Outlined.AutoAwesomeMotion,
            isSelected = true
        )
    }
}