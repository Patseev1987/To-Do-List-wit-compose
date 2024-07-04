package com.example.todolistwithcompose.domain

import com.example.todolistwithcompose.R

enum class TaskStatus(val idString: Int) {
    NOT_STARTED(R.string.not_started),IN_PROGRESS(R.string.in_progress),COMPLETED(R.string.completed);
}