package com.example.todolistwithcompose.presentor.viewModel

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.MainScreenState
import com.example.todolistwithcompose.utils.toTask
import com.example.todolistwithcompose.utils.toTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

class ViewModelMainScreen(
    private val context: Context
) : ViewModel() {
 private   val dao = TasksDatabase.getInstance(context).taskDao
    val state = dao.getTask()
        .map { it.map { taskEntity -> taskEntity.toTask() } }
        .map{ MainScreenState.Result(it) as MainScreenState }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MainScreenState.Loading
        )


    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
                dao.clearTaskById(task.id)
        }
    }

  suspend fun getTaskById(id: Long): Task {
         return dao.getTaskById(id)?.toTask() ?: throw RuntimeException("Task with id $id not found")
    }

}