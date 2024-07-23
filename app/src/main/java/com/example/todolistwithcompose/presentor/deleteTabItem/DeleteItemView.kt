package com.example.todolistwithcompose.presentor.deleteTabItem

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

@Composable
fun DeleteItemView(
    cancelButtonListener: () -> Unit,
    confirmButtonListener: () -> Unit,
) {
    val component = getApplicationComponent()
    val factory = component.getViewModelFactory()
    val viewModel = viewModel<DeleteTabItemViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(DeleteItemState.Loading)

    when (val currentState = state.value) {
        is DeleteItemState.Loading -> {
            CircularProgressIndicator()
        }

        is DeleteItemState.Result -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                MainPartDeleteItemView(
                    items = currentState.items,
                    label = viewModel.getLabel(),
                    cancelButtonListener = cancelButtonListener,
                    confirmButtonListener = {
                        viewModel.checkTaskFromTaskGroup(confirmButtonListener)
                    },
                    tabItemListener = {
                        viewModel.setTebItem(it)
                    }
                )
                if (currentState.isProblemWithTasks) {
                    MyItemDialog(
                        message = currentState.message,
                        onDismissRequest = { viewModel.setIsProblemWithTasks() },
                        onConfirmButtonClick = {
                            viewModel.deleteItem()
                            confirmButtonListener()
                        },
                        onDismissButtonClick = {
                            viewModel.setIsProblemWithTasks()
                        }
                    )
                }
                if (currentState.isError) {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.you_can_t_delete_all_tasks),
                        Toast.LENGTH_LONG
                    ).show()
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
                .weight(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.choose_tab_item_for_delete),
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.SansSerif,
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