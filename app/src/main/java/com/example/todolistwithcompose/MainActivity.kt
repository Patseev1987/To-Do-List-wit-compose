package com.example.todolistwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todolistwithcompose.navigation.AppNavGraph
import com.example.todolistwithcompose.navigation.Screen
import com.example.todolistwithcompose.navigation.rememberNavigationState
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTask
import com.example.todolistwithcompose.presentor.addTabItem.AddTabItem
import com.example.todolistwithcompose.presentor.deleteTabItem.DeleteItemView
import com.example.todolistwithcompose.presentor.mainScreen.StartScreen
import com.example.todolistwithcompose.presentor.showTask.ShowTask
import com.example.todolistwithcompose.presentor.theme.ui.ToDoListWithComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListWithComposeTheme {
                val navState = rememberNavigationState()
                AppNavGraph(
                    navController = navState.navHostController,
                    mainScreenContent = {
                        StartScreen(
                            onFABClickListener = { navState.navigateTo(Screen.AddTaskScreen.route) },
                            onTaskListener = { task ->
                                navState.navigateTo(Screen.ShowTaskScreen.getRouteWithArgs(task.id))
                            },
                            onAddTabItemListener = {
                                navState.navigateTo(Screen.AddTAbItemScreen.route)
                            },
                            onDeleteTabItemListener = {
                                navState.navigateTo(Screen.DeleteTAbItemScreen.route)
                            }
                        )
                    },
                    addScreenContent = {
                        AddAndUpdateTask(
                            onCancelListener = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            onButtonListener = {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        )
                    },
                    showTaskScreenContent = { taskId ->
                        ShowTask(
                            taskId = taskId,
                            updateClickListener = { id ->
                                navState.navigateToWithoutSaveState(Screen.UpdateScreen.getRouteWithArgs(id))
                            },
                            cancelClickListener = { onBackPressedDispatcher.onBackPressed() }
                        )
                    },
                    updateTaskScreenContent = { taskId ->
                        AddAndUpdateTask(taskId = taskId, onCancelListener = {
                            onBackPressedDispatcher.onBackPressed()
                        },
                            onButtonListener = {
                                onBackPressedDispatcher.onBackPressed()
                            })
                    },
                    addTAbItemContent = {
                        AddTabItem(
                            onButtonClick = { onBackPressedDispatcher.onBackPressed() },
                            onCancel = { onBackPressedDispatcher.onBackPressed() }
                        )
                    },
                    deleteTAbItemContent = {
                        DeleteItemView(
                            cancelButtonListener = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            confirmButtonListener = {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        )
                    }
                )
            }
        }
    }
}
