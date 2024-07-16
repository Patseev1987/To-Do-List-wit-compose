package com.example.todolistwithcompose.presentor.mainScreen

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.utils.toTabItem
import com.example.todolistwithcompose.utils.toTabItemEntity
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class TabViewModel @Inject constructor(
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {

    private var cacheTabs = listOf<TabItem>()
    private var cacheTasks = listOf<Task>()
    private var cacheSelectedTab = ALL_TASKS
    private val _state = MutableStateFlow<TabState>(TabState.Init)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkFirstStart()
            cacheSelectedTab = dao.getSelectedTabItem(true)?.toTabItem()
                ?: throw IllegalArgumentException("Selected TabItem is null")
            dao.getTabItems().map { entities ->
                entities.map { entity -> entity.toTabItem() }
            }.collect { tabs ->
                cacheTabs = tabs
                _state.value = TabState.Result(
                    tasks = cacheTasks.withFilter(cacheSelectedTab).specialSort(),
                    tabs = cacheTabs
                )
                Log.d("TabViewModel", "Tab collect: $cacheTabs \n $cacheTasks")
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            dao.getTasks().map { entities ->
                entities.map { entity -> entity.toTask() }
            }.collect { tasks ->
                cacheTasks = tasks
                _state.value = TabState.Result(
                    tasks = cacheTasks.withFilter(cacheSelectedTab).specialSort(),
                    tabs = cacheTabs
                )
                Log.d("TabViewModel", "Task collect: $cacheTabs \n $cacheTasks")
            }
        }
    }



    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTaskById(task.id)
        }
    }

    fun setSelected(tab: TabItem) {
        viewModelScope.launch {
            val oldSelected = dao.getSelectedTabItem()?.toTabItem()
                ?: throw IllegalStateException("Selected tab must be!")
            val unselected = oldSelected.copy(isSelected = false)
            val newSelected = tab.copy(isSelected = true)
            cacheSelectedTab = newSelected
            dao.insertTabItem(unselected.toTabItemEntity())
            dao.insertTabItem(newSelected.toTabItemEntity())
        }

    }

    private fun List<Task>.withFilter(tab: TabItem): List<Task> {
        return if (tab.name == ALL_TASKS.name) this else this.filter { it.tabItemName == tab.name }
    }

    private fun List<Task>.specialSort(): List<Task> = this.sortedWith(compareBy<Task>{ task -> task.status }
        .thenBy(nullsLast()) { task -> task.date })

    private suspend fun checkFirstStart() {
        val tabs = dao.getTabItems().firstOrNull() ?: emptyList()
        if (tabs.isEmpty()) {
            dao.insertTabItem(ALL_TASKS.toTabItemEntity())
        }
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