package com.example.todolistwithcompose.presentor.myUi

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.ToDoApplication
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.presentor.state.AddAndUpdateTabState
import com.example.todolistwithcompose.presentor.state.TabState
import com.example.todolistwithcompose.presentor.viewModel.AddAndUpdateTabItemViewModel
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddTabItem(
    tabItemName:String,
    onButtonClick: () -> Unit,
    onTabItemSelectedListener: () -> Unit,
    onTabItemUnselectedListener: () -> Unit,
    onCancel: () -> Unit,
) {
    val component = (LocalContext.current.applicationContext as ToDoApplication)
        .component.getSubComponentFactoryWithTabName().create(tabItemName)
    val factory = component.getViewModelFactory()
   val viewModel = viewModel<AddAndUpdateTabItemViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(initial = AddAndUpdateTabState.Loading)
    val snackbarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {paddingValues ->
        when (val currentSate = state.value ) {
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
                         onTabItemSelectedListener()
                     },
                     onTabItemUnselectedListener = {
                         onTabItemUnselectedListener()
                     }
                     )
                if (currentSate.errorMessage.isNotEmpty()){
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
    label:String,
    tabItem:TabItem,
    onTextChangeListener: (String) -> Unit,
    onCancelListener: () -> Unit,
    onOkListener: () -> Unit,
    onTabItemSelectedListener: () -> Unit,
    onTabItemUnselectedListener: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = tabItem.name,
            onValueChange = {
                onTextChangeListener(it)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        RowWithIconAndText(text = "Selected icon", icon = tabItem.selectedIcon) {
            onTabItemSelectedListener()
        }
        Spacer(modifier = Modifier.height(16.dp))
        RowWithIconAndText(text = "Unselected icon", icon = tabItem.unselectedIcon) {
            onTabItemUnselectedListener()
        }
        Spacer(modifier = Modifier.height(16.dp))
        MyButtons(
            label = label,
            addClickListener = { onOkListener() },
            cancelClickListener = { onCancelListener() }
        )
    }
}

@Composable
fun RowWithIconAndText(text:String, icon:ImageVector, onClickIconListener: () -> Unit){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
    ){
        Icon(
            imageVector =  icon,
            contentDescription = icon.name,
            modifier = Modifier.clickable { onClickIconListener() }
            )
        Text(text = text, fontSize = 18.sp)
    }
}