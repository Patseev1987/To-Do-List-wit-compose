package com.example.todolistwithcompose.presentor.myUi


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.todolistwithcompose.domain.TabItem
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AAA() {

    val tabs = TabItem.tabs
    val state = remember { mutableStateOf(0) }
    val pagerState = rememberPagerState {
        tabs.size
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if(!pagerState.isScrollInProgress) {
            state.value = pagerState.currentPage
        }
    }
    LaunchedEffect(state.value) {
        pagerState.animateScrollToPage(state.value)
    }
    Column {
       PrimaryTabRow (selectedTabIndex = state.value) {
            tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == state.value,
                        onClick = {
                            state.value = index

                                  },
                        text = { Text(text = tab.title) },
                        icon = { Icon(
                            imageVector = if (index == state.value) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = ""
                        ) }
                    )
            }
        }
        HorizontalPager(state = pagerState,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        ) { page ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = tabs[page].title)
            }

        }
    }
}


