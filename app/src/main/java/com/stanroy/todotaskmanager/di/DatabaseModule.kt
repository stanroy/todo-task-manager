package com.stanroy.todotaskmanager.di

import android.content.Context
import androidx.room.Room
import com.stanroy.todotaskmanager.data.source.local.TasksDatabase
import com.stanroy.todotaskmanager.data.source.local.dao.TasksDao
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {
    val provider: Module = module {
        single { provideDatabase(androidContext()) }
        single { provideDao(get()) }
    }

    private fun provideDatabase(context: Context): TasksDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TasksDatabase::class.java,
            "tasks_database"
        ).build()
    }

    private fun provideDao(tasksDatabase: TasksDatabase): TasksDao {
        return tasksDatabase.tasksDao()
    }
}