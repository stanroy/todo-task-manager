package com.stanroy.todotaskmanager.domain.usecase

import com.stanroy.todotaskmanager.domain.repository.TasksRepository

class MarkTaskAsFinishedUseCase(private val repository: TasksRepository) {

    suspend fun execute(taskId: Int) {
        repository.markTaskAsFinished(taskId)
    }

}