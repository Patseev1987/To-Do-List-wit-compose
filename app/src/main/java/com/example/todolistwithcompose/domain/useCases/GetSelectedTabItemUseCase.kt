package com.example.todolistwithcompose.domain.useCases

import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.TabItem
import javax.inject.Inject

class GetSelectedTabItemUseCase @Inject constructor (private val repository: ApplicationRepository) {
    suspend operator fun invoke(isSelected:Boolean = true):TabItem? {
      return  repository.getSelectedTabItem(isSelected = isSelected)
    }
}