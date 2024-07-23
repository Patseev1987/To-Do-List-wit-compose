package com.example.todolistwithcompose.presentor.showTask

import androidx.lifecycle.ViewModel
import com.example.todolistwithcompose.domain.newUseCases.GetShowTaskFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ShowTaskViewModel @Inject constructor(
    private val taskId: Long,
    private val showTaskFlowUseCase: GetShowTaskFlowUseCase,
    private val scope: CoroutineScope
) : ViewModel() {

    val state = showTaskFlowUseCase(taskId)
        .stateIn(
            scope = scope,
            initialValue = ShowTaskState.Loading,
            started = SharingStarted.Eagerly
        )
}