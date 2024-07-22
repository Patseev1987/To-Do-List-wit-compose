package com.example.todolistwithcompose.presentor.mainScreen

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


@Composable
fun StartScreen(
    onFABClickListener: () -> Unit,
    onTaskListener:(Task) -> Unit,
    onAddTabItemListener: () -> Unit,
    onDeleteTabItemListener: () -> Unit,
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
        MainScreen(
            modifier = Modifier.padding(paddingValues),
            onTaskListener = { onTaskListener(it) },
            onAddTabItemListener = { onAddTabItemListener() },
            onDeleteTabItemListener = { onDeleteTabItemListener() },
        )
    }
}