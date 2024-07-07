package com.example.todolistwithcompose.presentor.myUi

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import com.example.todolistwithcompose.utils.getBoarderColor
import com.example.todolistwithcompose.utils.getBoarderWidth
import com.example.todolistwithcompose.utils.getColor
import java.time.format.DateTimeFormatter


val DEFAULT_VALUE_FOR_SPACER = 8.dp

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
        colors = CardDefaults.cardColors(containerColor = task.getColor()),
        border = BorderStroke(width = task.getBoarderWidth(), task.getBoarderColor()),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                color = Color.Black,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = task.taskGroup.idString) ,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                )
                if (task.date != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = DateTimeFormatter
                                .ofPattern("HH:mm")
                                .format(task.date),
                            modifier = Modifier.padding(end = 20.dp),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = DateTimeFormatter
                                .ofPattern("yyyy-MM-dd")
                                .format(task.date),
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier,factory:ViewModelFactory ,  onTaskListener: (Task) -> Unit) {
    TabView(factory = factory, onTaskListener = { onTaskListener(it) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskWithFilter(tasks: List<Task>, onDismissListener: (Task) -> Unit, onTaskListener: (Task) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = tasks, key = { it.id }) { task ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { 400.dp.value }
            )
            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                onDismissListener(task)
            }
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                content = {
                    Task(
                        task = task,
                        onTaskListener = {
                            onTaskListener(task)
                        }
                    )
                },
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(color = Color.Red),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.delete_task),
                            color = Color.White,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier.padding(end = 20.dp)
                        )
                    }
                }
            )
        }
    }
}

