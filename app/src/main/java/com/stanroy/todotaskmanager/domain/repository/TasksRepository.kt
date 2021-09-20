package com.stanroy.todotaskmanager.domain.repository

import com.stanroy.todotaskmanager.domain.model.SingleTask
import kotlinx.coroutines.flow.Flow

interface TasksRepository {


    fun getAllTasks(): Flow<List<SingleTask>>


    suspend fun newTask(
        taskName: String,
        taskDueDate: Long,
        taskCategory: String,
        taskIsFinished: Boolean
    )

    suspend fun markTaskAsFinished(taskId: Int)

    suspend fun removeFinishedTasks()

    fun deleteTask(singleTask: SingleTask)

}