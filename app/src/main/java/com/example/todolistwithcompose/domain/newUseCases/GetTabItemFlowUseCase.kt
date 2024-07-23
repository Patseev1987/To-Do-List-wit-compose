package com.example.todolistwithcompose.domain.newUseCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.presentor.mainScreen.TabState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTabItemFlowUseCase @Inject constructor (private val repository: ApplicationRepository) {
    operator fun invoke(): Flow<TabState> {
        return repository.tabItemFlow()
    }
}