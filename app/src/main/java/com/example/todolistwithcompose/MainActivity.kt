package com.example.todolistwithcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todolistwithcompose.navigation.AppNavGraph
import com.example.todolistwithcompose.navigation.Screen
import com.example.todolistwithcompose.navigation.rememberNavigationState
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTask
import com.example.todolistwithcompose.presentor.mainScreen.*
import com.example.todolistwithcompose.presentor.showTask.ShowTask
import com.example.todolistwithcompose.presentor.theme.ui.ToDoListWithComposeTheme
import com.example.todolistwithcompose.presentor.ViewModelFactory
import com.example.todolistwithcompose.presentor.addTabItem.AddTabItem
import com.example.todolistwithcompose.presentor.deleteTabItem.DeleteItemView
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var factory: ViewModelFactory
    private val component by lazy {
        (this.application as ToDoApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListWithComposeTheme {
                val navState = rememberNavigationState()
                AppNavGraph(
                    navController = navState.navHostController,
                    mainScreenContent = {
                        StartScreen(
                            factory = factory,
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
                            factory = factory,
                            onButtonClick = { onBackPressedDispatcher.onBackPressed() },
                            onCancel = { onBackPressedDispatcher.onBackPressed() }
                        )
                    },
                    deleteTAbItemContent = {
                            DeleteItemView(
                                factory = factory,
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
