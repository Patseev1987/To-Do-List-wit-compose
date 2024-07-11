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

    private val loadingTaskFlow:MutableSharedFlow<TabState> = MutableSharedFlow()
    val state = dao.getTabItems().map { entities ->
        entities.map { it.toTabItem() }
    }.map {
        val selected = dao.getSelectedTabItem()?.name ?: throw IllegalArgumentException ()
        val tasks = dao.getTaskWithFilter(selected)
            .firstOrNull()
            ?.map { entity -> entity.toTask() } ?: emptyList()
        TabState.Result(tabs = it , task = tasks)as TabState
    }
        .mergeWith(loadingTaskFlow)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TabState.Init
    )

    private fun <T> Flow<T>.mergeWith(anotherFlow: Flow<T>): Flow<T> {
        return merge(this, anotherFlow)
    }

    fun emitInLoadingFlow(tabItem: TabItem){
        viewModelScope.launch {
              val task = dao.getTaskWithFilter(tabItem.name).map { it.map { it.toTask() } }.firstOrNull() ?: emptyList()
            loadingTaskFlow.emit(
                TabState.Result(task = task, tabs = (state.value as TabState.Result).tabs)
            )
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
        viewModelScope.launch {
            val oldSelected = dao.getSelectedTabItem()?.toTabItem()
                ?: throw IllegalStateException("Selected tab must be!")
            val unselected = oldSelected.copy(isSelected = false)
            val newSelected = tab.copy(isSelected = true)
            dao.insertTabItem(unselected.toTabItemEntity())
            dao.insertTabItem(newSelected.toTabItemEntity())
        }

    }


    private suspend fun updateTabItem( tabItem: TabItem){
        dao.insertTabItem(tabItem.toTabItemEntity())
    }


    private suspend fun checkFirstStart(){
        val tabs = dao.getTabItems().firstOrNull() ?: emptyList()
        if (tabs.isEmpty()) {
            dao.insertTabItem(ALL_TASKS.toTabItemEntity())
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