package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import javax.inject.Inject

class DeleteTabItemUseCase @Inject constructor (private val repository: ApplicationRepository) {
    suspend operator fun invoke(name: String) {
        repository.clearTabItemByName(name)
    }
}