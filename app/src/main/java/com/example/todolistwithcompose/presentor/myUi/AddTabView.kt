package com.example.todolistwithcompose.presentor.myUi

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory

@Composable
fun AddTabView(factory:ViewModelFactory){
   val viewModel = viewModel(factory = factory)
    val state = viewModel.state.value.collectAsState()
    Scaffold {padingValues ->
        when (val currentSate = state.value) {

        }

    }
}