package com.example.todolistwithcompose.presentor.myUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.state.ShowTaskState
import com.example.todolistwithcompose.presentor.viewModel.ShowTaskViewModel
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory


@Composable
fun ShowTask(taskId: Long, updateClickListener: (Long) -> Unit, cancelClickListener: () -> Unit) {
    val viewmodel = viewModel<ShowTaskViewModel>(factory = ViewModelFactory(LocalContext.current, taskId))
    val state = viewmodel.state.collectAsState(ShowTaskState.Loading)
    Scaffold { paddingValues ->
        when (val currentState = state.value) {
            is ShowTaskState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }

            is ShowTaskState.Result -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    MainPartShowTask(task = currentState.task, modifier = Modifier.weight(1f))
                    ShowTaskButtons(modifier = Modifier.padding(bottom = 35.dp),
                        updateClickListener = { updateClickListener(taskId) },
                        cancelClickListener = { cancelClickListener() }
                    )
                }
            }
        }

    }
}

@Composable
fun MainPartShowTask(task: Task, modifier: Modifier = Modifier) {
    Card(
        shape = CardDefaults.outlinedShape,
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.6f)),
        border = BorderStroke(2.dp, Color.Black),
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TitleLabel(task = task, modifier = Modifier.weight(3f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
                    .padding(start = 16.dp, end = 16.dp)
                    .border(BorderStroke(1.dp, Color.Black), shape = CircleShape.copy(CornerSize(8.dp)))
                    .background(Color.White),
                contentAlignment = Alignment.Center
                ) {
                Text(
                    text = task.content,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Text(
                text = "Task group:   ${task.getGroup()}",
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f)
            )

            if (task.date != null) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .weight(1f)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Text(text = "Time for a reminder:", color = Color.Black)
                        Text(
                            text = "${task.getDateForLabel()}    ${task.getTimeForLabel()}",
                            color = Color.Black
                        )
                    }

                }
            }

            Text(
                text = "Task status:   ${task.getStatusForLabel()}",
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ShowTaskButtons(modifier: Modifier = Modifier, updateClickListener: () -> Unit, cancelClickListener: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { updateClickListener() },
            shape = RectangleShape
        ) {
            Text(text = "Update task", fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = { cancelClickListener() },
            shape = RectangleShape
        ) {
            Text(text = "Cancel", fontSize = 20.sp)
        }
    }
}


@Composable
fun TitleLabel(task:Task, modifier: Modifier = Modifier) {
    Column(modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Title: ${task.title}",
            color = Color.Black,
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier
                .padding(top = 16.dp)
                .weight(1f)
        )
        Text(
            text = "Description:",
            color = Color.Black,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.weight(1f)
        )

    }
}

private fun Task.getTimeForLabel():String = this.date?.toLocalTime().toString()
private fun Task.getDateForLabel():String = this.date?.toLocalDate().toString()
private fun Task.getStatusForLabel() = this.status.value
private fun Task.getGroup() = this.taskGroup.value