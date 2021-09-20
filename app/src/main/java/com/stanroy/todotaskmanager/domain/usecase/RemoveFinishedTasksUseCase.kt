package com.stanroy.todotaskmanager.domain.usecase

import com.stanroy.todotaskmanager.domain.repository.TasksRepository

class RemoveFinishedTasksUseCase(private val repository: TasksRepository) {

    suspend fun execute() {
        repository.removeFinishedTasks()
    }
}