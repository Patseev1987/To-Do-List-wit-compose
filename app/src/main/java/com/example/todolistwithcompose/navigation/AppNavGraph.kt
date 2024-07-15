package com.example.todolistwithcompose.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainScreenContent: @Composable () -> Unit = {},
    addScreenContent: @Composable () -> Unit = {},
    taskScreenContent: @Composable () -> Unit = {},
    showTaskScreenContent: @Composable (taskId: Long) -> Unit = {},
    updateTaskScreenContent: @Composable (taskId: Long) -> Unit = {},
    workTaskScreenContent: @Composable () -> Unit = {},
    homeTaskScreenContent: @Composable () -> Unit = {},
    familyTaskScreenContent: @Composable () -> Unit = {},
    allTaskScreenContent: @Composable () -> Unit = {},
    addTAbItemContent: @Composable () -> Unit = {},
    deleteTAbItemContent: @Composable () -> Unit = {},
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

        composable(
            route = Screen.ShowTaskScreen.route,
            //all argument in path will be defined like string type
            //create argument like Long type
            arguments = listOf(navArgument(Screen.TASK_ID_KEY) {
                type = NavType.LongType
            })
        ) { //show_task_screen/{task_id}
            val taskId = it.arguments?.getLong(Screen.TASK_ID_KEY) ?: throw RuntimeException("Task not found")
            showTaskScreenContent(taskId)
        }

        composable(
            route = Screen.UpdateScreen.route,
            arguments = listOf(navArgument(Screen.TASK_ID_KEY) {
                type = NavType.LongType
            })
        ) {
            val taskId = it.arguments?.getLong(Screen.TASK_ID_KEY) ?: throw RuntimeException("Task not found")
            updateTaskScreenContent(taskId)
        }


        composable(
            route = Screen.WorkTaskScreen.route
        ){
            workTaskScreenContent()
        }

        composable(
            route = Screen.HomeTaskScreen.route
        ){
            homeTaskScreenContent()
        }

        composable(
            route = Screen.FamilyTaskScreen.route
        ){
            familyTaskScreenContent()
        }

        composable(
            route = Screen.AllTaskScreen.route
        ){
            allTaskScreenContent()
        }

        composable(
            route = Screen.AddTAbItemScreen.route
        ){
            addTAbItemContent()
        }
        composable(
            route = Screen.DeleteTAbItemScreen.route
        ){
            deleteTAbItemContent()
        }

    }
}