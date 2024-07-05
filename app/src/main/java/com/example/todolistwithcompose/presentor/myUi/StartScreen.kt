package com.example.todolistwithcompose.presentor.myUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory


@Composable
fun StartScreen(
    factory: ViewModelFactory,
    onFABClickListener: () -> Unit,
    onTaskListener:(Task) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                content = {
                    Image(imageVector = Icons.Default.Create, contentDescription = "")
                },
                onClick = {
                    onFABClickListener()
                },
                shape = CircleShape
            )
        }
    ) { paddingValues ->
        MainScreen(modifier = Modifier.padding(paddingValues),factory = factory ,onTaskListener = {onTaskListener(it)})
    }
}