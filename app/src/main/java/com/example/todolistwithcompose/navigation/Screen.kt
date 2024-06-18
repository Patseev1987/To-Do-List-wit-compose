package com.example.todolistwithcompose.navigation

sealed class Screen(val route: String) {
   data object MainScreen : Screen(MAIN_SCREEN)
   data object TaskScreen : Screen(TASK_SCREEN)
   data object AddTaskScreen : Screen(ADD_TASK_SCREEN)
   data object UpdateScreen : Screen(UPDATE_TASK_SCREEN)
   data object CompletedScreen : Screen(COMPLETED_TASK_SCREEN)
   data object InProgressScreen : Screen(IN_PROGRESS_TASK_SCREEN)
   data object NotStartedScreen : Screen(NOT_STARTED_TASK_SCREEN)



    companion object {
        const val MAIN_SCREEN = "main_screen"
        const val TASK_SCREEN = "task_screen"
        const val ADD_TASK_SCREEN = "add_screen"
        const val UPDATE_TASK_SCREEN = "update_screen"
        const val COMPLETED_TASK_SCREEN = "completed_screen"
        const val IN_PROGRESS_TASK_SCREEN = "in_progress_screen"
        const val NOT_STARTED_TASK_SCREEN = "not_started_screen"
    }
}