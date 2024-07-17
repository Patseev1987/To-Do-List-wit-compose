package com.example.todolistwithcompose.presentor.showTask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.useCases.GetTaskByIdUseCase
import com.example.todolistwithcompose.utils.toTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShowTaskViewModel @Inject constructor(
    private val taskId: Long,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
):ViewModel() {

    private val _state = MutableStateFlow<ShowTaskState>(ShowTaskState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val task = getTaskByIdUseCase(taskId) ?: throw Exception("Task not found")
            _state.value = ShowTaskState.Result(task)
        }
    }
}