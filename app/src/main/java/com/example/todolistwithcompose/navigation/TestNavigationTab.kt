package com.example.todolistwithcompose.navigation



import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun TestNavigationTab(
    navController: NavHostController,
    workTaskScreenContent: @Composable () -> Unit = {},
    homeTaskScreenContent: @Composable () -> Unit = {},
    familyTaskScreenContent: @Composable () -> Unit = {},
    allTaskScreenContent: @Composable () -> Unit = {},
) {
    NavHost(navController, startDestination = Screen.AllTaskScreen.route) {


        //Test Tab navigate

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

    }
}