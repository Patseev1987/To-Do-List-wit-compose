package com.example.todolistwithcompose.presentor.myUi

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.IconChoiceActivity
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.presentor.state.AddAndUpdateTabState
import com.example.todolistwithcompose.presentor.state.TabState
import com.example.todolistwithcompose.presentor.viewModel.AddAndUpdateTaskGroupViewModel
import com.example.todolistwithcompose.utils.selectedIcons
import com.example.todolistwithcompose.utils.unselectedIcons

private const val FILLED = "Filled"

@Composable
fun IconList(icons: List<ImageVector>, onClickListener: (ImageVector) -> Unit) {
    LazyVerticalGrid(modifier = Modifier.fillMaxWidth(), columns = GridCells.Fixed(3)) {
        items(items = icons, key = { icon -> icon.name }) { icon ->
            Icon(
                modifier = Modifier.clickable { onClickListener(icon) },
                imageVector = icon,
                contentDescription = icon.name
            )
        }
    }
}

@Composable
fun CreateTab(factory: ViewModelProvider.Factory) {
    val viewmodel = viewModel<AddAndUpdateTaskGroupViewModel>(factory = factory)
    val state = viewmodel.state.collectAsState(AddAndUpdateTabState.Loading)
    val context = LocalContext.current
    val snackbarHostState = SnackbarHostState()
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
    val launcher = rememberLauncherForActivityResult(contract = contract, onResult = {iconName ->
        if (iconName.contains(FILLED)) {
            viewmodel.setSelectedIcon(iconName)
        }else {
            viewmodel.setUnselectedIcon(iconName)
        }
    })


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        when(val currentState = state.value){
            is AddAndUpdateTabState.Loading -> { it }
            is AddAndUpdateTabState.Result -> {
                MainPartTabAdd(viewModel = viewmodel, launcher = launcher, context = context, currentState = currentState)
            }
            is AddAndUpdateTabState.Error -> {

            }

        }
    }
}

@Composable
fun SetIcon(icon:ImageVector, onClickListener: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.selected_icon))
        Spacer(modifier = Modifier.width(DEFAULT_VALUE_FOR_SPACER))
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            modifier = Modifier.clickable { onClickListener() }
        )
    }
}

@Composable
fun MainPartTabAdd(
    viewModel: AddAndUpdateTaskGroupViewModel,
    launcher: ManagedActivityResultLauncher<Intent, String>,
    context: Context,
    currentState: AddAndUpdateTabState.Result
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Set the title for task group:")
            Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
            TextField(value = currentState.tabItem.name, onValueChange = {
                viewModel.setTabName(it)
            })
            Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
            SetIcon(icon = currentState.tabItem.selectedIcon, onClickListener = {
                launcher.launch(IconChoiceActivity.getIntent(context,IconChoiceActivity.FILLED_TYPE_ICON))
            })
            Spacer(modifier = Modifier.height(DEFAULT_VALUE_FOR_SPACER))
            SetIcon(icon = currentState.tabItem.unselectedIcon, onClickListener = {
                launcher.launch(IconChoiceActivity.getIntent(context,IconChoiceActivity.OUTLINE_TYPE_ICON))
            })
        }
    }
}

