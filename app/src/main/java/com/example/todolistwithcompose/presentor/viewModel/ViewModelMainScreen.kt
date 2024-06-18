package com.example.todolistwithcompose.presentor.viewModel

import androidx.lifecycle.ViewModel
import com.example.todolistwithcompose.presentor.state.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelMainScreen: ViewModel() {
    private val _state = MutableStateFlow(MainScreenState.Initial)
    val state = _state.asStateFlow()


}