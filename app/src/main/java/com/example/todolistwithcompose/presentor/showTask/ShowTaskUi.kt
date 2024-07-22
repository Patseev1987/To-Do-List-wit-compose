package com.example.todolistwithcompose.presentor.showTask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.ToDoApplication
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.getApplicationComponent
import com.example.todolistwithcompose.presentor.mainScreen.DEFAULT_VALUE_FOR_SPACER


@Composable
fun ShowTask(taskId: Long, updateClickListener: (Long) -> Unit, cancelClickListener: () -> Unit) {
    val component = getApplicationComponent().getSubComponentFactory().create(taskId)
    val factory = component.getViewModelFactory()
    val viewmodel = viewModel<ShowTaskViewModel>(factory = factory)
    val state = viewmodel.state.collectAsState(ShowTaskState.Loading)
    Scaffold { paddingValues ->
       ShowTaskContent(
           paddingValues = paddingValues,
           state = state,
           updateClickListener = updateClickListener,
           cancelClickListener = cancelClickListener
       )
    }
}

@Composable
fun ShowTaskContent(
    paddingValues: PaddingValues,
    state: State<ShowTaskState>,
    updateClickListener: (Long) -> Unit,
    cancelClickListener: () -> Unit
){
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
                    updateClickListener = { updateClickListener(currentState.task.id) },
                    cancelClickListener = { cancelClickListener() }
                )
            }
        }
    }
}

@Composable
fun MainPartShowTask(task: Task, modifier: Modifier = Modifier) {
    Card(
        shape = CardDefaults.outlinedShape,
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.2f)),
        border = BorderStroke(2.dp, Color.Black),
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),

        ) {
        Column(horizontalAlignment = CenterHorizontally) {
            TitleLabel(task = task, modifier = Modifier.weight(2f))
            Content(task = task, modifier = Modifier.weight(5f))
            TaskInfo(task = task, modifier = Modifier.weight(3f))
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
            Text(text = stringResource(R.string.update_task), fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = { cancelClickListener() },
            shape = RectangleShape
        ) {
            Text(text = stringResource(R.string.cancel), fontSize = 20.sp)
        }
    }
}


@Composable
fun TitleLabel(task: Task, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = ParagraphStyle(
                        textAlign = TextAlign.Center
                    )
                ) {

                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(stringResource(R.string.title))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp
                    )
                ) {
                    append("   ${task.title}")
                }
            }
        )
        Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
        Text(
            text = stringResource(R.string.description),
            color = Color.Black,
            fontSize = 22.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun Content(modifier: Modifier = Modifier, task: Task) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        shape = CircleShape.copy(CornerSize(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = Color.Black)

    ) {
        val scrollState = rememberScrollState()
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {

            Text(
                text = task.content,
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TaskInfo(task: Task, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(R.string.task_group))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 25.sp
                    )
                )
                {
                    append("\n\n${task.tabItemName}")
                }
            })

        Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
        if (task.date != null) {
            TextWithDate(
                time = task.getTimeForLabel(),
                date = task.getDateForLabel(),
                textLabel = stringResource(id = R.string.time_for_a_reminder)
            )
        }
        if (task.completedDate != null) {
            TextWithDate(
                time = task.getCompletedTimeForLabel(),
                date = task.getCompletedDateForLabel(),
                textLabel = stringResource(id = R.string.completed_date)
            )
        }
        Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
        Text(
            modifier = Modifier,
            text = buildAnnotatedString {
                withStyle(
                    style = ParagraphStyle(
                        textAlign = TextAlign.Center
                    )
                ) {

                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(stringResource(R.string.task_status))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp
                    )
                ) {
                    append("   ${stringResource(id = task.status.idString)}")
                }
            }
        )
    }
}

@Composable
fun TextWithDate(time: String, date: String, textLabel:String) {
    Text(
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 8.dp),
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
            ) {
                append(textLabel)
            }
            append("\n\n")
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp,
                )
            ) {
                append("$time   $date")
            }
        }
    )
}

private fun Task.getTimeForLabel(): String = this.date?.toLocalTime().toString()
private fun Task.getDateForLabel(): String = this.date?.toLocalDate().toString()
private fun Task.getCompletedTimeForLabel(): String = this.completedDate?.toLocalTime().toString()
private fun Task.getCompletedDateForLabel(): String = this.completedDate?.toLocalDate().toString()

