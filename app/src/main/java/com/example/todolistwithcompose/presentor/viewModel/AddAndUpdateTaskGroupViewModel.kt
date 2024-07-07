package com.example.todolistwithcompose.presentor.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.AddAndUpdateTabState
import com.example.todolistwithcompose.presentor.state.AddAndUpdateTaskState
import com.example.todolistwithcompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject


class AddAndUpdateTaskGroupViewModel @Inject constructor(
    private val tabItemName:String? = null,
    private val appContext: Application,
    private val dao:Dao
) : ViewModel() {

    private lateinit var tabItem: TabItem
    private val _state: MutableStateFlow<AddAndUpdateTabState> = MutableStateFlow(AddAndUpdateTabState.Loading)
    val state = _state.asStateFlow()

    init {
        if (tabItemName.isNullOrBlank()) {
            tabItem = TabItem(DEFAULT_NAME)
            _state.value = AddAndUpdateTabState.Result(tabItem)
        }else {
            viewModelScope.launch(Dispatchers.IO) {
             dao.getTabItemByName(tabItemName).collect{
                 tabItem = it?.toTabItem() ?: throw RuntimeException("Unknown tabItem")
                 _state.value = AddAndUpdateTabState.Result(tabItem)
             }
            }
        }
    }

    fun setTabName(name: String) {
        tabItem = tabItem.copy(name = name)
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setSelectedIcon(selectedItemName: String) {
        tabItem = tabItem.copy(selectedIcon = selectedIcons.first{it.name == selectedItemName})
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setUnselectedIcon(selectedItemName: String) {
        tabItem = tabItem.copy(selectedIcon = unselectedIcons.first{it.name == selectedItemName})
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }



    fun saveTabItem(onButtonListener: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkTabItem(tabItem)){
                dao.insert(tabItem.toTabItemEntity())
            }
        }
    }

    private fun checkTabItem(tabItem: TabItem): Boolean {
        return !(tabItem.name == DEFAULT_NAME && tabItem.name.isNotEmpty())
    }


    companion object{
        private const val DEFAULT_NAME = "default_name"
    }


}