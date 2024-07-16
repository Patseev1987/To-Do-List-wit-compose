package com.example.todolistwithcompose.presentor.deleteTabItem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.TaskEntity
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import com.example.todolistwithcompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DeleteTabItemViewModel @Inject constructor(
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {
    var tabItem: TabItem? = null
    private val _state: MutableStateFlow<DeleteItemState> = MutableStateFlow(DeleteItemState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val items = dao
                .getTabItems()
                .map { entities ->
                    entities.map { entity ->
                        entity.toTabItem()
                    }
                }.firstOrNull()
                ?: throw IllegalArgumentException("Items must exist!")
            tabItem = items.first()
            _state.value = DeleteItemState.Result(items = items)
        }
    }

    fun getLabel() = appContext.getString(R.string.delete_task_group)
    fun setIsProblemWithTasks() {
        _state.value = (_state.value as DeleteItemState.Result).copy(isProblemWithTasks = false)
    }

    fun deleteItem() = viewModelScope.launch(Dispatchers.IO) {
        val tab = tabItem ?: return@launch
        dao.clearTabItemByName(tab.name)
        val taskIds = dao.getTasks()
            .firstOrNull()?.filter { entity -> entity.tabItemName == tab.name }
            ?.map { entity -> entity.id } ?: emptyList()
        taskIds.forEach { taskId -> dao.clearTaskById(taskId) }
        val selected = dao.getSelectedTabItem(true)
        if (selected?.name == null) {
            var tabItem = dao.getTabItemByName(TabViewModel.ALL_TASKS.name)
                ?: throw IllegalArgumentException("All tasks must be")
            tabItem = tabItem.copy(isSelected = true)
            dao.insertTabItem(tabItem)
        }
    }

    fun checkTaskFromTaskGroup(callBack: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        if (tabItem?.name == TabViewModel.ALL_TASKS.name) {
            _state.value = (_state.value as DeleteItemState.Result).copy(isError = true)
            return@launch
        }
        val tab = tabItem ?: return@launch
        val tasks = dao.getTasks()
            .firstOrNull()?.filter { entity -> entity.tabItemName == tab.name } ?: emptyList()
        if (tasks.isNotEmpty()) {
            _state.value = (_state.value as DeleteItemState.Result).copy(
                isProblemWithTasks = true,
                message = getMessage(tasks)
            )
        } else {
            deleteItem()
            withContext(Dispatchers.Main) {
                callBack()
            }
        }
    }

    private fun getMessage(tasks: List<TaskEntity>): String {
        return appContext.getString(
            R.string.you_have_tasks_with_group_are_you_sure_you_want_to_delete_this_tasks,
            tasks.size,
            tabItem?.name
        )
    }
}