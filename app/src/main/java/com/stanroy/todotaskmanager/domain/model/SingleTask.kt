package com.stanroy.todotaskmanager.domain.model

data class SingleTask(
    val id: Int,
    val taskName: String,
    val taskDueDate: Long,
    val taskCategory: TaskCategory,
    var taskIsFinished: Boolean = false
)