package com.stanroy.todotaskmanager.domain.usecase

import com.stanroy.todotaskmanager.domain.model.SingleTask
import com.stanroy.todotaskmanager.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(private val repository: TasksRepository) {
    suspend fun execute(): Flow<List<SingleTask>> {
        return repository.getAllTasks()
    }
}