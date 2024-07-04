package com.example.todolistwithcompose.presentor.myUi


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.state.TabState
import com.example.todolistwithcompose.presentor.viewModel.TabViewModel
import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabView(onTaskListener: (Task) -> Unit) {
    val viewModel = viewModel<TabViewModel>(factory = ViewModelFactory(LocalContext.current))
    val stateViewModel by viewModel.state.collectAsState()
    val tabs = viewModel.tabs
    val state = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            state.intValue = page
            viewModel.getTasks(page)
        }
    }

    Column {
        TabRow(selectedTabIndex = tabs.size) {
            tabs.forEach { tab ->
                Tab(
                    selected = tab.tabId == state.intValue,
                    onClick = {
                        state.intValue = tab.tabId
                        scope.launch {
                            pagerState.animateScrollToPage(tab.tabId)
                        }
                    },
                    text = { Text(text = stringResource(id = tab.titleId)) },
                    icon = {
                        Icon(
                            imageVector = if (tab.tabId == state.intValue) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = ""
                        )
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            userScrollEnabled = false
        ) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                when (val currentState = stateViewModel){
                    is TabState.Result -> {
                        TaskWithFilter(
                            tasks = currentState.task,
                            onDismissListener = { viewModel.deleteTask(it) },
                            onTaskListener = { onTaskListener(it) })
                    }

                    is TabState.Init -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


