package com.example.todolistwithcompose.presentor.mainScreen

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.tabEntity.TabItemEntity
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


class TabViewModel @Inject constructor (
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {


    private var cacheTabs = listOf<TabItem>()
    private var cacheTasks = listOf<Task>()
    private val _state: MutableStateFlow<TabState> = MutableStateFlow(TabState.Init)
    val state = _state.asStateFlow()

   init {
       viewModelScope.launch(Dispatchers.IO) {
            dao.getTabItems().map{ entities ->
                entities.map { entity -> entity.toTabItem() }
            }.collect{
                if (it.isEmpty()){
                    dao.insertTabItem(ALL_TASKS.toTabItemEntity())
                    val tabsList = listOf(dao.getSelectedTabItem()?.toTabItem()
                        ?: throw IllegalStateException("Selected tab is null"))
                        cacheTabs = tabsList
                    _state.value = TabState.Result(
                        task = cacheTasks,
                        tabs = cacheTabs.sortedBy{tab -> tab.id}
                    )
                }
                cacheTabs = it
                _state.value = TabState.Result(
                    task = emptyList(),
                    tabs = cacheTabs.sortedBy{tab -> tab.id}
                )
            }
       }
       viewModelScope.launch(Dispatchers.IO) {
           dao.getTask().map{ entities ->
               entities.map { task -> task.toTask() }
           }.collect{ tasks ->
               cacheTasks = tasks
               _state.value = TabState.Result(
                   task = tasks,
                   tabs = cacheTabs.sortedBy{task -> task.id}
               )
           }
       }
   }




    fun deleteTabItem(tab: TabItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTabItemByName(tab.name)
        }
    }

    fun deleteTask (task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTaskById(task.id)
        }
    }

    fun setSelected(tab:TabItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTabSelected = tab.copy(isSelected = true)
            val oldSelectedTab = dao.getSelectedTabItem()?.copy(isSelected = false)
                ?: throw IllegalStateException("tab selected is null")
            dao.insertTabItem(oldSelectedTab)
            dao.insertTabItem(newTabSelected.toTabItemEntity())
        }
    }


    companion object{
        val ALL_TASKS = TabItem(
            name = "All",
            selectedIcon = Icons.Filled.AutoAwesomeMotion,
            unselectedIcon = Icons.Outlined.AutoAwesomeMotion,
            isSelected = true
        )
    }
}