package com.example.todolistwithcompose.domain.newUseCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTaskState
import com.example.todolistwithcompose.presentor.showTask.ShowTaskState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShowTaskFlowUseCase @Inject constructor (private val repository: ApplicationRepository) {
    operator fun invoke(taskId:Long): Flow<ShowTaskState> {
        return repository.showTaskFlow(taskId)
    }
}