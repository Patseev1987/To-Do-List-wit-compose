package com.example.todolistwithcompose.domain.newUseCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTaskState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddAndUpdateTaskCase @Inject constructor (private val repository: ApplicationRepository) {
    operator fun invoke(taskId:Long): Flow<AddAndUpdateTaskState> {
        return repository.addAnUpdateTaskFlow(taskId = taskId)
    }
}