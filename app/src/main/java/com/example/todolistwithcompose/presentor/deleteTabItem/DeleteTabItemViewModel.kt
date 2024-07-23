package com.example.todolistwithcompose.presentor.deleteTabItem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.TaskEntity
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.newUseCases.DeleteItemFlowUseCase
import com.example.todolistwithcompose.domain.useCases.*
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import com.example.todolistwithcompose.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DeleteTabItemViewModel @Inject constructor(
    private val appContext: Application,
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteTabItemUseCase: DeleteTabItemUseCase,
    private val getTabItemByNameUseCase: GetTabItemByNameUseCase,
    private val getSelectedTabItemUseCase: GetSelectedTabItemUseCase,
    private val insertTabItemUseCase: InsertTabItemUseCase,
    private val scope: CoroutineScope,
    private val deleteItemFlowUseCase: DeleteItemFlowUseCase
) : ViewModel() {
    var tabItem: TabItem? = null
    private val _state: MutableStateFlow<DeleteItemState> = MutableStateFlow(DeleteItemState.Loading)
    val state = deleteItemFlowUseCase()
        .onEach {
            if (it is DeleteItemState.Result)
            tabItem = it.items.first()
        }
        .mergeWith(_state.asStateFlow())
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = DeleteItemState.Loading
        )

    fun getLabel() = appContext.getString(R.string.delete_task_group)
    fun setIsProblemWithTasks() {
        _state.value = (_state.value as DeleteItemState.Result).copy(isProblemWithTasks = false)
    }

    fun deleteItem() = viewModelScope.launch(Dispatchers.IO) {
        val tab = tabItem ?: return@launch
        deleteTabItemUseCase(tab.name)
        val taskIds = getTasksUseCase()
            .firstOrNull()
            ?.filter { entity -> entity.tabItemName == tab.name }
            ?.map { entity -> entity.id } ?: emptyList()
        taskIds.forEach { taskId -> deleteTaskUseCase(taskId) }
        val selected = getSelectedTabItemUseCase()
        if (selected?.name == null) {
            var tabItem = getTabItemByNameUseCase(TabViewModel.ALL_TASKS.name)
            tabItem = tabItem.copy(isSelected = true)
            insertTabItemUseCase(tabItem)
        }
    }

    fun checkTaskFromTaskGroup(callBack: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        if (tabItem?.name == TabViewModel.ALL_TASKS.name) {
            _state.value = (_state.value as DeleteItemState.Result).copy(isError = true)
            return@launch
        }
        val tab = tabItem ?: return@launch
        val tasks = getTasksUseCase()
            .firstOrNull()
            ?.filter { entity -> entity.tabItemName == tab.name } ?: emptyList()
        if (tasks.isNotEmpty()) {
            val currentValue = _state.value
            if (currentValue is DeleteItemState.Result) {
              _state.value  = currentValue.copy(
                    isProblemWithTasks = true,
                    message = getMessage(tasks)
                )
            }
        } else {
            deleteItem()
            withContext(Dispatchers.Main) {
                callBack()
            }
        }
    }

    private fun getMessage(tasks: List<Task>): String {
        return appContext.getString(
            R.string.you_have_tasks_with_group_are_you_sure_you_want_to_delete_this_tasks,
            tasks.size,
            tabItem?.name
        )
    }
}