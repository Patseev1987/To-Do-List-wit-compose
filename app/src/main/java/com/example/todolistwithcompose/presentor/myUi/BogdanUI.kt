package com.example.todolistwithcompose.presentor.myUi

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.MainScreenState
import com.example.todolistwithcompose.presentor.state.ShowTaskState
import com.example.todolistwithcompose.presentor.theme.ui.MyGrayForCard
import com.example.todolistwithcompose.presentor.theme.ui.Pink80
import com.example.todolistwithcompose.presentor.viewModel.AddTaskViewModel
import com.example.todolistwithcompose.presentor.viewModel.ShowTaskViewModel
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import com.example.todolistwithcompose.presentor.viewModel.ViewModelMainScreen
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


val DEFAULT_SPACE_FOR_SPACER = 10.dp

@SuppressLint("SimpleDateFormat")
@Composable
fun Task(task: Task, onTaskListener: (Task) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable {
                onTaskListener(task)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MyGrayForCard)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = task.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.content,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val color = when (task.taskGroup) {
                TaskGroup.WORK_TASK -> Pink80
                TaskGroup.FAMILY_TASK -> Color.Yellow
                TaskGroup.HOME_TASK -> Color.Blue
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = task.taskGroup.value,
                    color = color,
                    modifier = Modifier.weight(1f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("HH:mm")
                            .format(task.date),
                        modifier = Modifier.padding(end = 20.dp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("yyyy-MM-dd")
                            .format(task.date)
                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, onTaskListener: (Task) -> Unit) {
    val viewModel = viewModel<ViewModelMainScreen>(factory = ViewModelFactory(LocalContext.current))
    val state = viewModel.state.collectAsState(MainScreenState.Initial)
    when (val currentState = state.value) {
        is MainScreenState.Loading -> {

        }

        is MainScreenState.Error -> {

        }

        is MainScreenState.Initial -> {

        }

        is MainScreenState.Result -> {
            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = currentState.notes, key = { it.id }) { task ->
                    val dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        viewModel.deleteTask(task)
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        dismissContent = {
                            Task(
                                task = task,
                                onTaskListener = {
                                    onTaskListener(task)
                                }
                            )
                        },
                        background = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .background(color = Color.Red),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(text = "DELETE TASK", color = Color.White)
                            }
                        }
                    )
                }
            }
        }
    }


}



