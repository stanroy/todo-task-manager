package com.stanroy.todotaskmanager.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.stanroy.todotaskmanager.data.source.local.dto.SingleTaskDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM singletaskdto")
    fun getAllTasks(): Flow<List<SingleTaskDto>>

    @Query("SELECT * FROM singletaskdto WHERE id LIKE :taskId")
    suspend fun findTaskById(taskId: Int): SingleTaskDto

    @Insert
    suspend fun newTask(singleTask: SingleTaskDto)

    @Query("UPDATE singletaskdto SET task_is_finished=1 WHERE id=:taskId")
    suspend fun markTaskAsFinished(taskId: Int)

    @Query("DELETE from singletaskdto WHERE task_is_finished LIKE 1")
    suspend fun removeFinishedTasks()

    @Delete
    fun deleteTask(singleTask: SingleTaskDto)

}