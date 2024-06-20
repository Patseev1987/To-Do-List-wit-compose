package com.example.todolistwithcompose.presentor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.AddTaskState
import com.example.todolistwithcompose.utils.toTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime



class AddTaskViewModel(context: Context) : ViewModel() {
    private val taskDao = TasksDatabase.getInstance(context = context).taskDao

    private var task: Task = Task(
        title = "",
        content = "",
        date = LocalDateTime.now(),
        taskGroup = TaskGroup.WORK_TASK,
        status = TaskStatus.NOT_STARTED
    )

    private val _state: MutableStateFlow<AddTaskState> = MutableStateFlow(AddTaskState.Result(task))
    val state = _state.asStateFlow()


    fun setTitle(title: String) {
        task.title = title
        _state.value = AddTaskState.Result(task)
    }

    fun setContent(content: String) {
        task.content = content
        _state.value = AddTaskState.Result(task)
    }

    fun setTaskGroup(value: String) {
        val taskGroup = TaskGroup.entries.first { it.value == value }
        task = task.copy(taskGroup = taskGroup)
        _state.value = AddTaskState.Result(task)
    }

    fun setStatus(value: String) {
        val status = TaskStatus.entries.first { it.value == value }
        task.status = status
        _state.value = AddTaskState.Result(task)
    }

    fun setTime(time: LocalTime) {
        val date = task.date?.toLocalDate()
        val newDate = LocalDateTime.of(date, time)
        task = task.copy(date = newDate)
        _state.value = AddTaskState.Result(task)
    }

    fun setDate(date: LocalDate) {
        val time = task.date?.toLocalTime()
        val newDate = LocalDateTime.of(date, time)
        task = task.copy(date = newDate)
        _state.value = AddTaskState.Result(task)
    }

    fun saveTask() {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkTask(task)) {
                taskDao.insert(task.toTaskEntity())
            }
        }
    }


    private fun checkTask(task: Task): Boolean {
        return when {
            task.title.isEmpty() -> {
                _state.value = AddTaskState.Result(task, errorTitle = true)
                false}
            task.content.isEmpty() -> {
                _state.value = AddTaskState.Result(task, errorContext = true)
                false}
            else -> true
        }
    }
}