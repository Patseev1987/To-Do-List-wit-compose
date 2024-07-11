package com.example.todolistwithcompose.presentor.mainScreen


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.ViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabView(
    factory: ViewModelFactory,
    onTaskListener: (Task) -> Unit,
    onAddTabItemListener: () -> Unit
) {

    val viewModel = viewModel<TabViewModel>(factory = factory)
    val stateViewModel by viewModel.state.collectAsState(TabState.Init)

    when (val currentState = stateViewModel) {
        is TabState.Init -> {
            Text(text = "INIT")
        }

        is TabState.Result -> {

            val pagerState = rememberPagerState { currentState.tabs.size }
            val scope = rememberCoroutineScope()

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect { page ->

                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { onAddTabItemListener() }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                    var selectedIndex = 0

                    currentState.tabs.forEachIndexed { index, tab -> if (tab.isSelected) selectedIndex = index }

                    ScrollableTabRow(
                        selectedTabIndex = selectedIndex,
                        modifier = Modifier.weight(1f)
                    ) {
                        for ((index, tab) in currentState.tabs.withIndex()) {
                            Tab(
                                selected = tab.isSelected,
                                onClick = {
                                    viewModel.emitInLoadingFlow(tab)
                                    viewModel.setSelected(tab)
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = { Text(text = tab.name) },
                                icon = {
                                    Icon(
                                        imageVector = if (tab.isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.name
                                    )
                                }
                            )
                        }
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
                        TaskWithFilter(
                            tasks = currentState.task,
                            onDismissListener = { viewModel.deleteTask(it) },
                            onTaskListener = { onTaskListener(it) })

                    }
                }
            }
        }
    }
}


