package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor (private val repository: ApplicationRepository){
    operator fun invoke(id: Long): Flow<Task?> {
       return repository.getTaskById(id)
    }
}