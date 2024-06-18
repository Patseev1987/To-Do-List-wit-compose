package com.example.todolistwithcompose.presentor.theme.ui

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.presentor.viewModel.ViewModelMainScreen
import java.text.SimpleDateFormat
import java.time.LocalDate


@Composable
fun Task(title: String, content: String, time: String, groupLabel: TaskGroup, date: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MyGrayForCard)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val color = when (groupLabel) {
                TaskGroup.WORK_TASK -> Color.Magenta
                TaskGroup.FAMILY_TASK -> Color.Yellow
                TaskGroup.HOME_TASK -> Color.Blue
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = groupLabel.value,
                    color = color,
                    modifier = Modifier.weight(1f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = time,
                        modifier = Modifier.padding(end = 20.dp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = date
                    )
                }

            }
        }
    }

}


@Composable
@Preview
fun TaskPreview() {
    Task(
        title = "Note title",
        content = "Note content",
        time = "12:45",
        groupLabel = TaskGroup.WORK_TASK,
        date = "2024-12-12"
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ViewModelMainScreen) {
    val state by viewModel.state.collectAsState(listOf())

    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items =  state, key = {it.id}) { task ->
        val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                viewModel.deleteTask(task)
            }
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissContent = {
                    Task(
                        title = task.title,
                        content = task.content,
                        groupLabel = task.taskGroup,
                        date = SimpleDateFormat("yyyy-MM-dd").format(task.date),
                        time = SimpleDateFormat("HH-mm").format(task.date)
                    )
                },
                background = {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                        .background(color = Color.Red)) {
                        Text(text = "DELETE TASK", color = Color.White)
                    }
                }
            )


        }
    }
}