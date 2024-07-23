package com.example.todolistwithcompose.presentor.addAndUpdateTask

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.getApplicationComponent
import com.example.todolistwithcompose.presentor.mainScreen.DEFAULT_VALUE_FOR_SPACER
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
fun AddAndUpdateTask(
    modifier: Modifier = Modifier,
    taskId: Long = 0L,
    onCancelListener: () -> Unit,
    onButtonListener: () -> Unit
) {
    val component = getApplicationComponent().getSubComponentFactory().create(taskId)
    val factory = component.getViewModelFactory()
    val viewmodel = viewModel<AddAndUpdateTaskViewModel>(factory = factory)
    val state = viewmodel.state.collectAsState(initial = AddAndUpdateTaskState.Loading)

    val snackbarHostState = SnackbarHostState()
    val launcherNotificationPermission = rememberLauncherForActivityResult(
        contract = RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewmodel.saveTask(onButtonListener)
            } else {
                viewmodel.permissionsDenied()
            }
        }
    )
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        AddAndUpdateTaskContent(
            paddingValues = paddingValues,
            state = state,
            snackbarHostState = snackbarHostState,
            modifier = modifier,
            onChangeTitleListener = { viewmodel.setTitle(it) },
            onChangeContentListener = { viewmodel.setContent(it) },
            onChangeDataListener = { viewmodel.setDate(it) },
            onChangeTimeListener = { viewmodel.setTime(it) },
            onSelectedStatusListener = { viewmodel.setStatus(it) },
            onMenuClickListener = { viewmodel.setGroup(it) },
            onCheckedListener = { viewmodel.changeIsRemind() },
            label = viewmodel.getLabel(),
            addClickListener = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcherNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    viewmodel.saveTask(onButtonListener)
                }
            },
            cancelClickListener = { onCancelListener() }
        )
    }
}


@Composable
fun AddAndUpdateTaskContent(
    paddingValues: PaddingValues,
    state: State<AddAndUpdateTaskState>,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier,
    onSelectedStatusListener: (status: String) -> Unit,
    onChangeTitleListener: (title: String) -> Unit,
    onChangeContentListener: (content: String) -> Unit,
    onChangeDataListener: (LocalDate) -> Unit,
    onChangeTimeListener: (LocalTime) -> Unit,
    onMenuClickListener: (TabItem) -> Unit,
    onCheckedListener: () -> Unit,
    addClickListener: () -> Unit,
    cancelClickListener: () -> Unit,
    label: String
) {
    when (val currentState = state.value) {
        is AddAndUpdateTaskState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                val scrollState = rememberScrollState()
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    MainPartForAddAndUpdateTask(
                        currentState,
                        label = label,
                        tabs = currentState.tabs,
                        onChangeTitleListener = { onChangeTitleListener(it) },
                        onChangeContentListener = { onChangeContentListener(it) },
                        onChangeDataListener = { onChangeDataListener(it) },
                        onChangeTimeListener = { onChangeTimeListener(it) },
                        onSelectedStatusListener = { onSelectedStatusListener(it) },
                        onMenuClickListener = { onMenuClickListener(it) },
                        onCheckedListener = { onCheckedListener() }
                    )
                    Spacer(modifier = Modifier.height(40.dp))


                    MyButtons(
                        label = label,
                        addClickListener = { addClickListener() },
                        cancelClickListener = { cancelClickListener() })
                }
            }
            ShowSnackbares(currentState = currentState, snackbarHost = snackbarHostState)
        }
    }
}

@Composable
fun MainPartForAddAndUpdateTask(
    currentState: AddAndUpdateTaskState.Result,
    label: String,
    tabs: List<TabItem>,
    onSelectedStatusListener: (status: String) -> Unit,
    onChangeTitleListener: (title: String) -> Unit,
    onChangeContentListener: (content: String) -> Unit,
    onChangeDataListener: (LocalDate) -> Unit,
    onChangeTimeListener: (LocalTime) -> Unit,
    onMenuClickListener: (TabItem) -> Unit,
    onCheckedListener: () -> Unit
) {
    Text(text = label, fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    OutlinedTextField(
        value = currentState.task.title,
        isError = currentState.errorTitle,
        onValueChange = {
            onChangeTitleListener(it)
        },
        label = {
            Text(text = stringResource(id = R.string.title))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Text(text = label, fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    OutlinedTextField(
        currentState.task.content,
        isError = currentState.errorContext,
        onValueChange = {
            onChangeContentListener(it)
        },
        label = {
            Text(text = stringResource(id = R.string.description))
        },
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp)
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Text(
        text = stringResource(R.string.choose_the_task_group),
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    ExposedDropDownMenuWithTabItems(tabs = tabs, selected = currentState.task.tabItemName) {
        onMenuClickListener(it)
    }
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.set_date_and_time_for_remind),
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif
        )
        Checkbox(
            checked = currentState.task.isRemind,
            onCheckedChange = {
                onCheckedListener()
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = MaterialTheme.colorScheme.background
            )

        )
    }
    if (currentState.task.isRemind) {
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
        text = stringResource(R.string.set_status_for_task),
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    )
    Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
    RadioButtonsStatus(stringResource(id = currentState.task.status.idString)) {
        onSelectedStatusListener(it)
    }
}


@Composable
fun RadioButtons(values: List<String>, selected: String, onSelectedListener: (String) -> Unit) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(selected) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropDownMenuWithTabItems(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>,
    selected: String,
    onClickListener: (TabItem) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selected) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tabs.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            onClickListener(item)
                            selectedText = item.name
                            expanded = false
                            Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RadioButtonsStatus(selected: String, onSelectedListener: (String) -> Unit) {
    val statues = listOf(
        stringResource(id = TaskStatus.NOT_STARTED.idString),
        stringResource(id = TaskStatus.IN_PROGRESS.idString),
        stringResource(id = TaskStatus.COMPLETED.idString),
    )
    RadioButtons(statues, selected) {
        onSelectedListener(it)
    }
}

@Composable
fun MyDataPicker(
    startDate: LocalDate = LocalDate.now(),
    startTime: LocalTime = LocalTime.now(),
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

    Row {
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
                Text(text = stringResource(R.string.pick_time))
            }
            Text(text = formattedTime)
        }

    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.ok)) {
            }
            negativeButton(text = stringResource(R.string.cancel))
        }
    ) {
        datepicker(
            initialDate = startDate,
            title = stringResource(R.string.pick_a_date),
        ) {
            pickedDate = it
            dateChangeListener(it)
        }
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.ok)) {
            }
            negativeButton(text = stringResource(R.string.cancel))
        }
    ) {
        timepicker(
            is24HourClock = true,
            initialTime = startTime,
            title = stringResource(R.string.pick_a_time)
        ) {
            pickedTime = it
            timeChangeListener(it)
        }
    }
}

@Composable
fun MyButtons(
    label: String,
    addClickListener: () -> Unit,
    cancelClickListener: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(50.dp),
        modifier = modifier
    ) {
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
            Text(text = stringResource(id = R.string.cancel))
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowSnackbares(currentState: AddAndUpdateTaskState.Result, snackbarHost: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    val messageDateError = stringResource(R.string.date_shouldn_t_be_early_than) +
            "\n${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))}"
    val messageTitleError = stringResource(R.string.title_shouldn_t_be_empty)
    val messageContentError = stringResource(R.string.description_shouldn_t_be_empty)
    val messageIsGranted = stringResource(R.string.permissions_denied)

    scope.launch {
        when {
            currentState.errorDate -> snackbarHost.showSnackbar(
                message = messageDateError,
                duration = SnackbarDuration.Short
            )

            currentState.errorTitle -> snackbarHost.showSnackbar(
                message = messageTitleError,
                duration = SnackbarDuration.Short
            )

            currentState.errorContext -> snackbarHost.showSnackbar(
                message = messageContentError,
                duration = SnackbarDuration.Short
            )

            !currentState.isGranted -> snackbarHost.showSnackbar(
                message = messageIsGranted,
                duration = SnackbarDuration.Short
            )
        }
    }
}
