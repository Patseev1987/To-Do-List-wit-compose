package com.example.todolistwithcompose.presentor.addTabItem

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.IconChoiceActivity
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.presentor.ViewModelFactory
import com.example.todolistwithcompose.presentor.addAndUpdateTask.MyButtons
import com.example.todolistwithcompose.utils.selectedIcons
import com.example.todolistwithcompose.utils.unselectedIcons
import kotlinx.coroutines.launch


private const val FILLED = "Filled"


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddTabItem(
    factory:ViewModelFactory,
    onButtonClick: () -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel = viewModel<AddTabItemViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(initial = AddAndUpdateTabState.Loading)
    val snackbarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    val contract = object : ActivityResultContract<Intent, String>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String {
            if (resultCode == Activity.RESULT_OK) {
                val iconName = intent?.getStringExtra(IconChoiceActivity.EXTRA_ICON_NAME)
                return when (val iconType = intent?.getIntExtra(IconChoiceActivity.EXTRA_TYPE_ICON, -1)) {
                    IconChoiceActivity.FILLED_TYPE_ICON -> selectedIcons
                        .map { it.name }
                        .first { it == iconName }

                    IconChoiceActivity.OUTLINE_TYPE_ICON -> unselectedIcons
                        .map { it.name }
                        .first { it == iconName }

                    else -> throw IllegalArgumentException("Unknown icon type $iconType")
                }

            }
            throw IllegalArgumentException("Unknown icon type")
        }
    }
    val launcher = rememberLauncherForActivityResult(contract = contract, onResult = { iconName ->
        if (iconName.contains(FILLED)) {
            viewModel.setSelectedIcon(iconName)
        } else {
            viewModel.setUnselectedIcon(iconName)
        }
    })

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        when (val currentSate = state.value) {
            is AddAndUpdateTabState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
                paddingValues
            }

            is AddAndUpdateTabState.Result -> {

                MainPartAddTabItem(
                    label = viewModel.getLabel(),
                    tabItem = currentSate.tabItem,
                    onTextChangeListener = {
                        viewModel.setTabName(it)
                    },
                    onCancelListener = {
                        onCancel()
                    },
                    onOkListener = {
                        viewModel.saveTabItem {
                            onButtonClick()
                        }
                    },
                    onTabItemSelectedListener = {
                        launcher.launch(
                            IconChoiceActivity
                                .getIntent(context = context, IconChoiceActivity.FILLED_TYPE_ICON)
                        )
                    },
                    onTabItemUnselectedListener = {
                        launcher.launch(
                            IconChoiceActivity
                                .getIntent(context = context, IconChoiceActivity.OUTLINE_TYPE_ICON)
                        )
                    }
                )
                if (currentSate.errorMessage.isNotEmpty()) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(currentSate.errorMessage)
                    }
                }
            }
        }

    }
}

@Composable
fun MainPartAddTabItem(
    label: String,
    tabItem: TabItem,
    onTextChangeListener: (String) -> Unit,
    onCancelListener: () -> Unit,
    onOkListener: () -> Unit,
    onTabItemSelectedListener: () -> Unit,
    onTabItemUnselectedListener: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = TextStyle(fontSize = 20.sp))
        Spacer(modifier = Modifier.size(20.dp))
        TextField(
            singleLine = true,
            textStyle = TextStyle().copy(fontSize = 24.sp),
            value = tabItem.name,
            onValueChange = {
                onTextChangeListener(it)
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        RowWithIconAndText(text = "Selected icon", icon = tabItem.selectedIcon) {
            onTabItemSelectedListener()
        }
        Spacer(modifier = Modifier.height(40.dp))
        RowWithIconAndText(text = "Unselected icon", icon = tabItem.unselectedIcon) {
            onTabItemUnselectedListener()
        }
        Spacer(modifier = Modifier.height(40.dp))
        MyButtons(
            label = label,
            addClickListener = { onOkListener() },
            cancelClickListener = { onCancelListener() }
        )
    }
}

@Composable
fun RowWithIconAndText(text: String, icon: ImageVector, onClickIconListener: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            modifier = Modifier
                .size(100.dp)
                .clickable { onClickIconListener() }
        )
        Text(text = text, fontSize = 24.sp)
    }
}