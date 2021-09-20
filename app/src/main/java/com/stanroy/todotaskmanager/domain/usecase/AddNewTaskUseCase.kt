package com.stanroy.todotaskmanager.domain.usecase

import com.stanroy.todotaskmanager.domain.repository.TasksRepository

class AddNewTaskUseCase(private val repository: TasksRepository) {
    suspend fun execute(
        taskName: String,
        taskDueDate: Long,
        taskCategory: String,
        taskIsFinished: Boolean
    ) {

        // Room will autogenerate id in data layer
        repository.newTask(taskName, taskDueDate, taskCategory, taskIsFinished)
    }
}