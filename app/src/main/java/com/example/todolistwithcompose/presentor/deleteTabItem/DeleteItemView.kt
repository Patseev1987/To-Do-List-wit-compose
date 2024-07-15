package com.example.todolistwithcompose.presentor.deleteTabItem

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.presentor.ViewModelFactory
import com.example.todolistwithcompose.presentor.addAndUpdateTask.ExposedDropDownMenuWithTabItems

@Composable
fun DeleteItemView(
    factory: ViewModelFactory
) {
    val context = LocalContext.current
    val viewModel = viewModel<DeleteTabItemViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(DeleteItemState.Loading)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val currentState = state.value) {
            is DeleteItemState.Loading -> {}
            is DeleteItemState.Result -> {
                MainPartDeleteItemView(items = currentState.items)
            }
        }
    }
}

@Composable
fun MainPartDeleteItemView(items: List<TabItem>) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top =  36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(R.string.choose_tab_item_for_delete),
            fontSize = 25.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(40.dp))
        ExposedDropDownMenuWithTabItems(
            tabs = items,
        ) {

        }
    }

}