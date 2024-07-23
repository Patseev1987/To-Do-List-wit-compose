package com.example.todolistwithcompose.domain.newUseCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTaskState
import com.example.todolistwithcompose.presentor.deleteTabItem.DeleteItemState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteItemFlowUseCase @Inject constructor (private val repository: ApplicationRepository) {
    operator fun invoke(): Flow<DeleteItemState> {
        return repository.deleteItemFlow()
    }
}