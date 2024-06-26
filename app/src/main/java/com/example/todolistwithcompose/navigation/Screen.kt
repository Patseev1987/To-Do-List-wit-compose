package com.example.todolistwithcompose.navigation

import com.example.todolistwithcompose.navigation.Screen.ShowTaskScreen.SHOW_TASK_ROUTE

sealed class Screen(val route: String) {
   data object MainScreen : Screen(MAIN_SCREEN)
   data object TaskScreen : Screen(TASK_SCREEN)
   data object AddTaskScreen : Screen(ADD_TASK_SCREEN)
   data object UpdateScreen : Screen(UPDATE_TASK_SCREEN){
       private const val UPDATE_TASK_ROUTE = "update_screen"
       fun getRouteWithArgs(taskId: Long) = "$UPDATE_TASK_ROUTE/$taskId"
   }
   data object ShowTaskScreen : Screen(SHOW_TASK_SCREEN) {
       private const val SHOW_TASK_ROUTE = "show_task_screen"
       fun getRouteWithArgs(taskId: Long) = "$SHOW_TASK_ROUTE/$taskId"
   }



    companion object {
        const val TASK_ID_KEY = "task_id"
        const val MAIN_SCREEN = "main_screen"
        const val TASK_SCREEN = "task_screen"
        const val ADD_TASK_SCREEN = "add_screen"
        const val UPDATE_TASK_SCREEN = "update_screen/{$TASK_ID_KEY}"
        const val SHOW_TASK_SCREEN = "show_task_screen/{$TASK_ID_KEY}"
    }
}