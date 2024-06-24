package com.example.todolistwithcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainScreenContent: @Composable () -> Unit = {},
    addScreenContent: @Composable () -> Unit = {},
    taskScreenContent: @Composable () -> Unit = {},
) {
    NavHost(navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            mainScreenContent()
        }

        composable(Screen.AddTaskScreen.route) {
            addScreenContent()
        }
        composable(Screen.TaskScreen.route) {
            taskScreenContent()
        }

    }
}