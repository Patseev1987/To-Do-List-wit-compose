package com.example.todolistwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todolistwithcompose.navigation.AppNavGraph
import com.example.todolistwithcompose.navigation.Screen
import com.example.todolistwithcompose.presentor.myUi.AddAndUpdateTask
import com.example.todolistwithcompose.presentor.myUi.StartScreen
import com.example.todolistwithcompose.navigation.rememberNavigationState
import com.example.todolistwithcompose.presentor.myUi.ShowTask
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
                            }
                        )
                    },
                    addScreenContent = {
                        AddAndUpdateTask(
                            onCancelListener =  {
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
                            updateClickListener = { taskId ->

                            },
                            cancelClickListener = { onBackPressedDispatcher.onBackPressed() }
                        )
                    }
                )
            }
        }
    }
}
