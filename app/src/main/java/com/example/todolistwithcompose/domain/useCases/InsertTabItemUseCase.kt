package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.TabItem
import javax.inject.Inject

class InsertTabItemUseCase @Inject constructor(private val repository: ApplicationRepository) {
         suspend operator fun invoke(tabItem: TabItem) {
            repository.insertTabItem(tabItem)
        }
}