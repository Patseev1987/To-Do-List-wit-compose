package com.example.todolistwithcompose.domain

import android.content.Context
import com.example.todolistwithcompose.R


enum class TaskGroup(val idString: Int) {
    WORK_TASK (R.string.work_task), HOME_TASK (R.string.home_task), FAMILY_TASK (R.string.family_task);
}