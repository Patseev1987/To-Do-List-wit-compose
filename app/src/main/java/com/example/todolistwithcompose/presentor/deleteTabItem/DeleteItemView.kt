package com.example.todolistwithcompose.presentor.deleteTabItem

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.getApplicationComponent
import com.example.todolistwithcompose.presentor.addAndUpdateTask.ExposedDropDownMenuWithTabItems
import com.example.todolistwithcompose.presentor.addAndUpdateTask.MyButtons
import kotlinx.coroutines.launch

@Composable
fun DeleteItemView(
    cancelButtonListener: () -> Unit,
    confirmButtonListener: () -> Unit,
) {
    val component = getApplicationComponent()
    val factory = component.getViewModelFactory()
    val viewModel = viewModel<DeleteTabItemViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(DeleteItemState.Loading)
    val snackbarHostState = SnackbarHostState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        DeleteTAbItemContent(
            state = state,
            cancelButtonListener = cancelButtonListener,
            confirmButtonListener = {
                viewModel.checkTaskFromTaskGroup(confirmButtonListener)
            },
            label = viewModel.getLabel(),
            tabItemListener = {
                viewModel.setTebItem(it)
            },
            onDismissRequest = { viewModel.setIsProblemWithTasks() },
            onDismissButtonClick =   {
                viewModel.setIsProblemWithTasks()
            },
            onConfirmButtonClick ={
                viewModel.deleteItem()
                confirmButtonListener()
            },
            snackbarHostState = snackbarHostState
        )
    }

}

@Composable
fun DeleteTAbItemContent(
    state: State<DeleteItemState>,
    cancelButtonListener: () -> Unit,
    confirmButtonListener: () -> Unit,
    label:String,
    tabItemListener: (TabItem) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState
){
    val scope = rememberCoroutineScope()
    when (val currentState = state.value) {
        is DeleteItemState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }

        is DeleteItemState.Result -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainPartDeleteItemView(
                    items = currentState.items,
                    label = label,
                    cancelButtonListener = cancelButtonListener,
                    confirmButtonListener = confirmButtonListener,
                    tabItemListener = tabItemListener
                )
                if (currentState.isProblemWithTasks) {
                    MyItemDialog(
                        message = currentState.message,
                        onDismissRequest = onDismissRequest,
                        onConfirmButtonClick = onConfirmButtonClick,
                        onDismissButtonClick = onDismissButtonClick
                    )
                }
                if (currentState.isError) {
                    val message = stringResource(R.string.you_can_t_delete_all_tasks)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainPartDeleteItemView(
    items: List<TabItem>, label: String,
    confirmButtonListener: () -> Unit,
    cancelButtonListener: () -> Unit,
    tabItemListener: (TabItem) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.7f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.choose_tab_item_for_delete),
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(150.dp))
            ExposedDropDownMenuWithTabItems(
                tabs = items, selected = items[0].name
            ) {
                tabItemListener(it)
            }
        }
        MyButtons(
            modifier = Modifier.weight(0.3f),
            label = label,
            addClickListener = { confirmButtonListener() },
            cancelClickListener = { cancelButtonListener() }
        )
    }
}

@Composable
fun MyItemDialog(
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            OutlinedButton(
                onClick = {
                    onConfirmButtonClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismissButtonClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.dismiss),
                )
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                "Info",
                tint = Color.Red
            )
        },
        title = {
            Text(stringResource(R.string.are_you_sure))
        },
        text = {
            Text(text = message, textAlign = TextAlign.Center)
        }

    )
}