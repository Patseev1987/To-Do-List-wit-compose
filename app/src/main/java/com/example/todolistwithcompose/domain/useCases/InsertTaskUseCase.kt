package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.Task
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(private val repository: ApplicationRepository) {
         suspend operator fun invoke(task: Task) {
            repository.insert(task)
        }
}