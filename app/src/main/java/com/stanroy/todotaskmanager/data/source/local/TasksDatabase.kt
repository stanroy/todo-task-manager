package com.stanroy.todotaskmanager.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stanroy.todotaskmanager.data.source.local.dao.TasksDao
import com.stanroy.todotaskmanager.data.source.local.dto.SingleTaskDto

@Database(entities = [SingleTaskDto::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
}