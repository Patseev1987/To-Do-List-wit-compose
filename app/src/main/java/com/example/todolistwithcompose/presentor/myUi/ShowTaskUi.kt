package com.example.todolistwithcompose.presentor.myUi

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.selection.selectableGroup
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
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
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
                Text(text = "Loading...", modifier = Modifier.padding(paddingValues))
            }
            is ShowTaskState.Result -> {
                Column (modifier = Modifier.fillMaxSize()) {
                    MainPartShowTask(task = currentState.task, modifier = Modifier.weight(1f))
                    ShowTaskButtons( modifier = Modifier.padding(bottom = 35.dp),
                        updateClickListener = {updateClickListener(taskId)},
                        cancelClickListener = {cancelClickListener()}
                    )
                }
            }
        }

    }
}

@Composable
fun MainPartShowTask(task: Task , modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Spacer(modifier = Modifier.height(DEFAULT_SPACE_FOR_SPACER*2))
        Text(text = task.title, color = Color.Black, fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(DEFAULT_SPACE_FOR_SPACER*2))
        Text(text = task.content, color = Color.Black, fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(DEFAULT_SPACE_FOR_SPACER*2))
        ShowTaskRadioButtonsTaskGroup(selected = task.taskGroup.value)
        Spacer(modifier = Modifier.height(DEFAULT_SPACE_FOR_SPACER*2))
        Row() {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(text = "Date:", color = Color.Black)
                Text(text = task.date?.toLocalDate().toString(), color = Color.Black)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(text = "Time:", color = Color.Black)
                Text(text = task.date?.toLocalTime().toString(), color = Color.Black)
            }
        }
            Spacer(modifier = Modifier.height(DEFAULT_SPACE_FOR_SPACER*2))
            ShowTaskRadioButtonsStatus(task.status.value)
    }
}

@Composable
fun ShowTaskButtons( modifier: Modifier = Modifier, updateClickListener: () -> Unit, cancelClickListener: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { updateClickListener() },
            shape = RectangleShape
        ){
            Text(text = "Update task", fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = { cancelClickListener() },
            shape = RectangleShape
        ){
            Text(text = "Cancel", fontSize = 20.sp)
        }
    }
}

@Composable
fun ShowTaskRadioButtons(values: List<String>, selected: String) {
    Column(Modifier.selectableGroup()) {
        values.forEach { text ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selected),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ShowTaskRadioButtonsTaskGroup(selected: String){
    val taskGroups = listOf(
        TaskGroup.WORK_TASK.value,
        TaskGroup.HOME_TASK.value,
        TaskGroup.FAMILY_TASK.value,
    )
    ShowTaskRadioButtons(taskGroups, selected)
}

@Composable
fun ShowTaskRadioButtonsStatus(selected: String) {
    val statues = listOf(
        TaskStatus.NOT_STARTED.value,
        TaskStatus.IN_PROGRESS.value,
        TaskStatus.COMPLETED.value,
    )
    ShowTaskRadioButtons(statues, selected)
}