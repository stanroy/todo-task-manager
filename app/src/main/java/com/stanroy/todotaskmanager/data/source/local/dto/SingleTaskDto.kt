package com.stanroy.todotaskmanager.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SingleTaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "task_name")
    val taskName: String,
    @ColumnInfo(name = "task_date")
    val taskDueDate: Long,
    @ColumnInfo(name = "task_category")
    val taskCategory: String,
    @ColumnInfo(name = "task_is_finished")
    val taskIsFinished: Boolean = false
)