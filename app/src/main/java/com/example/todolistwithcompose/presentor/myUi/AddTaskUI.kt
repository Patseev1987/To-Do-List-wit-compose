package com.example.todolistwithcompose.presentor.myUi

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.AddAndUpdateTaskState
import com.example.todolistwithcompose.presentor.viewModel.AddAndUpdateTaskViewModel
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddAndUpdateTask(modifier: Modifier = Modifier,taskId:Long = 0L, onCancelListener: () -> Unit, onButtonListener: () -> Unit) {
    val viewmodel = viewModel<AddAndUpdateTaskViewModel>(factory = ViewModelFactory(LocalContext.current, taskId = taskId))
    val state = viewmodel.state.collectAsState(initial = AddAndUpdateTaskState.Loading)
    val currentState = state.value
    val snackbarHostState = SnackbarHostState()
    Scaffold(
        snackbarHost = {
           SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        when (currentState) {
            is AddAndUpdateTaskState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }
            is AddAndUpdateTaskState.InitState -> {
            }

            is AddAndUpdateTaskState.Result -> {
                Box(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        modifier = modifier.fillMaxWidth(),
                    ) {
                        MainPartForAddAndUpdateTask(
                            currentState,
                            label = viewmodel.getLabel(),
                            onChangeTitleListener = {viewmodel.setTitle(it)},
                            onChangeContentListener = {viewmodel.setContent(it)},
                            onChangeDataListener = {viewmodel.setDate(it)},
                            onChangeTimeListener = {viewmodel.setTime(it)},
                            onSelectedStatusListener = {viewmodel.setStatus(it)},
                            onSelectedGroupListener = {viewmodel.setTaskGroup(it)},
                            onCheckedListener = {viewmodel.changeIsRemind()}
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        MyButtons(
                            label = viewmodel.getLabel(),
                            addClickListener =  { viewmodel.saveTask(onButtonListener) },
                            cancelClickListener = { onCancelListener() })
                    }
                }
                ShowSnackbares(currentState = currentState, snackbarHost = snackbarHostState)
                }
            }
        }
    }


@Composable
fun MainPartForAddAndUpdateTask(
    currentState: AddAndUpdateTaskState.Result,
    label: String,
    onSelectedGroupListener: (group: String) -> Unit,
    onSelectedStatusListener: (status: String) -> Unit,
    onChangeTitleListener: (title: String) -> Unit,
    onChangeContentListener: (content: String) -> Unit,
    onChangeDataListener: (LocalDate) -> Unit,
    onChangeTimeListener: (LocalTime) -> Unit,
    onCheckedListener: () -> Unit
) {
    Text(text = label, color = Color.Black, fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    OutlinedTextField(
        value = currentState.task.title,
        isError = currentState.errorTitle,
        onValueChange = {
            onChangeTitleListener(it)
        },
        label = {
            Text(text = "Title")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Text(text = "$label content", color = Color.Black, fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    OutlinedTextField(
        currentState.task.content,
        isError = currentState.errorContext,
        onValueChange = {
            onChangeContentListener(it)
        },
        label = {
            Text(text = "Content")
        },
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp)
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Text(
        text = "Choose the task group",
        color = Color.Black,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    RadioButtonsTaskGroup(selected = currentState.task.taskGroup.value) {
        onSelectedGroupListener(it)
    }
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Set date and time for remind",
            color = Color.Black,
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif
        )
        Checkbox(checked = currentState.task.isRemind, onCheckedChange = {
            onCheckedListener()
        })
    }
    if (currentState.task.isRemind){
        Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
        MyDataPicker(
            startDate = currentState.task.date?.toLocalDate() ?: LocalDate.now(),
            startTime = currentState.task.date?.toLocalTime() ?: LocalTime.now(),
            dateChangeListener = {
                onChangeDataListener(it)
            },
            timeChangeListener = {
                onChangeTimeListener(it)
            }
        )
    }
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Text(
        text = "Set status for task",
        color = Color.Black,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    RadioButtonsStatus(currentState.task.status.value) {
        onSelectedStatusListener(it)
    }
}


@Composable
fun RadioButtons(values: List<String>, selected: String, onSelectedListener: (String) -> Unit) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(values[0]) }
    Column(Modifier.selectableGroup()) {
        values.forEach { text ->
            Row(
                Modifier
                    .height(36.dp)
                    .selectable(
                        selected = (text == selected),
                        onClick = {
                            onOptionSelected(text)
                            onSelectedListener(text)
                        },
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
fun RadioButtonsTaskGroup(selected: String, onSelectedListener: (String) -> Unit) {
    val taskGroups = listOf(
        TaskGroup.WORK_TASK.value,
        TaskGroup.HOME_TASK.value,
        TaskGroup.FAMILY_TASK.value,
    )
    RadioButtons(taskGroups, selected) {
        onSelectedListener(it)
    }
}

@Composable
fun RadioButtonsStatus(selected: String, onSelectedListener: (String) -> Unit) {
    val statues = listOf(
        TaskStatus.NOT_STARTED.value,
        TaskStatus.IN_PROGRESS.value,
        TaskStatus.COMPLETED.value,
    )
    RadioButtons(statues, selected) {
        onSelectedListener(it)
    }
}

@Composable
fun MyDataPicker(
    startDate:LocalDate = LocalDate.now(),
    startTime:LocalTime = LocalTime.now(),
    dateChangeListener: (LocalDate) -> Unit,
    timeChangeListener: (LocalTime) -> Unit
) {
    var pickedDate by remember {
        mutableStateOf(startDate)
    }
    var pickedTime by remember {
        mutableStateOf(startTime)
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd-MM-yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    Row() {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = CenterHorizontally
        ) {
            OutlinedButton(onClick = {
                dateDialogState.show()
            }) {
                Text(text = "Pick date")
            }
            Text(text = formattedDate)
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = CenterHorizontally
        ) {
            OutlinedButton(onClick = {
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
            initialDate = startDate,
            title = "Pick a date",
        ) {
            pickedDate = it
            dateChangeListener(it)
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
            initialTime = startTime,
            title = "Pick a time"
        ) {
            pickedTime = it
            timeChangeListener(it)
        }
    }
}

@Composable
fun MyButtons(label: String, addClickListener: () -> Unit, cancelClickListener: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
        OutlinedButton(
            onClick = { addClickListener() },
            shape = RectangleShape,
        ) {
            Text(text = label)
        }
        OutlinedButton(
            onClick = { cancelClickListener() },
            shape = RectangleShape
        ) {
            Text(text = "Cancel")
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowSnackbares(currentState:AddAndUpdateTaskState.Result, snackbarHost: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    scope.launch {
        when {
            currentState.errorDate -> snackbarHost.showSnackbar(
                message = "Date shouldn't be early than " +
                        "\n${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))}",
                duration = SnackbarDuration.Short
            )

            currentState.errorTitle -> snackbarHost.showSnackbar(
                message = "Title shouldn't be empty!",
                duration = SnackbarDuration.Short
            )

            currentState.errorContext -> snackbarHost.showSnackbar(
                message = "Description shouldn't be empty!",
                duration = SnackbarDuration.Short
            )
        }
    }

}
