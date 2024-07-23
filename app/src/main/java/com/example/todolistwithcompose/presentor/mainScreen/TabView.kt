package com.example.todolistwithcompose.presentor.mainScreen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.getApplicationComponent
import kotlinx.coroutines.launch


@Composable
fun TabView(
    modifier: Modifier,
    onTaskListener: (Task) -> Unit,
    onAddTabItemListener: () -> Unit,
    onDeleteTabItemListener: () -> Unit,
) {
    val component = getApplicationComponent()
    val factory = component.getViewModelFactory()
    val viewModel = viewModel<TabViewModel>(factory = factory)
    val stateViewModel = viewModel.state.collectAsState(TabState.Loading)

    TabViewContent(
        modifier = modifier,
        stateViewModel = stateViewModel,
        onTaskListener = onTaskListener,
        onAddTabItemListener = onAddTabItemListener,
        onDeleteTabItemListener = onDeleteTabItemListener,
        onClick = {
            viewModel.setSelected(it)
        },
        onDismissListener = { viewModel.deleteTask(it) }
    )

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabViewContent(
    modifier: Modifier,
    stateViewModel: State<TabState>,
    onTaskListener: (Task) -> Unit,
    onAddTabItemListener: () -> Unit,
    onDeleteTabItemListener: () -> Unit,
    onClick: (TabItem) -> Unit,
    onDismissListener: (Task) -> Unit
) {
    when (val currentState = stateViewModel.value) {
        is TabState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TabState.Result -> {
            val pagerState = rememberPagerState { currentState.tabs.size }
            val scope = rememberCoroutineScope()

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    MySegmentButton(
                        onPlusClicked = { onAddTabItemListener() },
                        onMinusClicked = { onDeleteTabItemListener() },
                    )
                    Spacer(Modifier.width(8.dp))
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
                                    onClick(tab)
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
                            tasks = currentState.tasks,
                            onDismissListener = { onDismissListener(it) },
                            onTaskListener = { onTaskListener(it) })
                    }
                }
            }
        }
    }
}

@Composable
fun MySegmentButton(
    modifier: Modifier = Modifier,
    onPlusClicked: () -> Unit,
    onMinusClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .width(40.dp)
                .height(30.dp)
                .clickable { onPlusClicked() }
        ) {
            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .width(40.dp)
                .height(30.dp)
                .clickable { onMinusClicked() }
        ) {
            Text(text = "-", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
    }
}