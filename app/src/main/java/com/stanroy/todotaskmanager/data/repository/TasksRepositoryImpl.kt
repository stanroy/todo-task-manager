package com.stanroy.todotaskmanager.data.repository

import com.stanroy.todotaskmanager.data.source.local.dao.TasksDao
import com.stanroy.todotaskmanager.data.source.local.dto.SingleTaskDto
import com.stanroy.todotaskmanager.domain.model.SingleTask
import com.stanroy.todotaskmanager.domain.model.TaskCategory
import com.stanroy.todotaskmanager.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(private val tasksDao: TasksDao) : TasksRepository {


    override fun getAllTasks(): Flow<List<SingleTask>> {
        return tasksDao.getAllTasks().map { list ->
            list.map { it.toSingleTask() }
        }
    }

    override suspend fun newTask(
        taskName: String,
        taskDueDate: Long,
        taskCategory: String,
        taskIsFinished: Boolean
    ) {
        tasksDao.newTask(
            SingleTask(
                0,
                taskName,
                taskDueDate,
                taskCategory.toTaskCategory(),
                false
            ).toSingleTaskDto()
        )
    }

    override suspend fun markTaskAsFinished(taskId: Int) {
        tasksDao.markTaskAsFinished(taskId)
    }

    override suspend fun removeFinishedTasks() {
        tasksDao.removeFinishedTasks()
    }

    override fun deleteTask(singleTask: SingleTask) {
        tasksDao.deleteTask(singleTask.toSingleTaskDto())
    }

    private fun String.toTaskCategory(): TaskCategory {
        return when {
            this == "WORK" -> {
                TaskCategory.WORK
            }
            this == "SHOPPING" -> {
                TaskCategory.SHOPPING
            }

            this == "PERSONAL" -> {
                TaskCategory.PERSONAL
            }

            this == "HOBBY" -> {
                TaskCategory.HOBBY
            }

            this == "OTHERS" -> {
                TaskCategory.OTHERS
            }

            else -> {
                TaskCategory.OTHERS
            }
        }
    }

    private fun SingleTaskDto.toSingleTask(): SingleTask {
        return SingleTask(
            id = id,
            taskName = taskName,
            taskDueDate = taskDueDate,
            taskCategory = taskCategory.toTaskCategory(),
            taskIsFinished = taskIsFinished
        )
    }

    private fun SingleTask.toSingleTaskDto(): SingleTaskDto {
        return SingleTaskDto(
            taskName = taskName,
            taskDueDate = taskDueDate,
            taskCategory = taskCategory.toString(),
            taskIsFinished = taskIsFinished
        )
    }
}