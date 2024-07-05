package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor (private val repository: ApplicationRepository) {
    suspend operator fun invoke(id: Long) {
        repository.clearTaskById(id)
    }
}