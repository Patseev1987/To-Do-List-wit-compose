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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class TabViewModel @Inject constructor (
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {

    private val _state: MutableStateFlow<TabState> = MutableStateFlow(TabState.Init)
    val state = _state.asStateFlow()
    private var _tabs:List<TabItem> = emptyList()

   init {
       viewModelScope.launch(Dispatchers.IO) {

           dao.getTabItems().map { entities ->
               val tabs:MutableList<TabItemEntity> = mutableListOf()
               if (entities.isEmpty()) {
                   dao.insertTabItem(ALL_TASKS.toTabItemEntity())
                   tabs.add(dao.getTabItemByName(ALL_TASKS.name) ?: throw IllegalArgumentException())
               }
               tabs
           }.map { entities ->
               entities.map { entity -> entity.toTabItem()
               }
           }.collect {tabs ->
           dao.getTask().map { entities ->
               entities.map { task -> task.toTask() }
           }.collect { tasks ->
               _state.value = TabState.Result(
                   tabs = tabs,
                   task = tasks
               )
           }
           }
       }
   }






    fun loadData( tab: TabItem ) {
    viewModelScope.launch(Dispatchers.IO) {
        var tabs:List<TabItem> = listOf()
            dao.getTabItems().map { entities ->
            entities.map { entity -> entity.toTabItem() }
        }.collect { tabs = it }
        dao.getTask().map { entity -> entity.map { it.toTask() } }
            .collect{ tasks ->
                val tasksList = if (tab.name != ALL_TASKS.name)
                    tasks.filter { it.tabItemName == tab.name }
                else tasks
                _state.value = TabState.Result(tasksList, tabs)
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

    fun setSelected(newSelected:TabItem, oldSelected:TabItem) {
        viewModelScope.launch(Dispatchers.IO) {

        }

    }


    private suspend fun updateTabItem( tabItem: TabItem){
        dao.insertTabItem(tabItem.toTabItemEntity())
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