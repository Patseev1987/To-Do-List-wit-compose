package com.example.todolistwithcompose.presentor.myUi

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.presentor.theme.ui.MyGrayForCard
import com.example.todolistwithcompose.presentor.theme.ui.Pink80
import com.example.todolistwithcompose.presentor.viewModel.ViewModelMainScreen
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


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
                        text = SimpleDateFormat("HH-mm").format(task.date),
                        modifier = Modifier.padding(end = 20.dp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = SimpleDateFormat("yyyy-MM-dd").format(task.date)
                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ViewModelMainScreen, onTaskListener: (Task) -> Unit, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState(listOf())

    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = state, key = { it.id }) { task ->
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

@Composable
@Preview
fun AddTask(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(horizontalAlignment = CenterHorizontally,
            modifier = modifier.fillMaxWidth()) {
            Text(text = "Add Task", color = Color.Black, fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.height(8.dp))
            var textForTitle by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(textForTitle, onValueChange = { textForTitle = it }, label = {
                Text(text = "Title")
            })
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Add task content", color = Color.Black, fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.height(8.dp))
            var textForContent by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(textForContent, onValueChange = { textForContent = it }, label = {
                Text(text = "Content")
            }, modifier = Modifier.height(100.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Choose the task group", color = Color.Black, fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.height(8.dp))
            RadioButtonsGroup()
            Spacer(modifier = Modifier.height(8.dp))
                MyDataPicker()

        }
    }
}

@Composable
fun RadioButtonsGroup(){
    val radioOptions = listOf(
        TaskGroup.WORK_TASK.value,
        TaskGroup.HOME_TASK.value,
        TaskGroup.FAMILY_TASK.value,
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .height(36.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
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
@Preview
fun MyDataPicker(){
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MM dd yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column{
            Button(onClick = {
                dateDialogState.show()
            }) {
                Text(text = "Pick date")
            }
            Text(text = formattedDate)
        }

        Spacer(modifier = Modifier.width(16.dp))
        Column{
            Button(onClick = {
                timeDialogState.show()
            }) {
                Text(text = "Pick time")
            }
            Text(text = formattedTime)
        }

    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
        ) {
            pickedDate = it
        }
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
            }
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker(
            is24HourClock = true,
            initialTime = LocalTime.NOON,
            title = "Pick a time"
        ) {
            pickedTime = it
        }
    }
}
