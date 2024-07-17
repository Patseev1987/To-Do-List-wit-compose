package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTabItemsUseCase @Inject constructor (private val repository: ApplicationRepository) {
    operator fun invoke(): Flow<List<TabItem>> {
        return repository.getTabItems()
    }
}