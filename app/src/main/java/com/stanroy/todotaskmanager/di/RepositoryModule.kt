package com.stanroy.todotaskmanager.di

import com.stanroy.todotaskmanager.data.repository.TasksRepositoryImpl
import com.stanroy.todotaskmanager.data.source.local.dao.TasksDao
import com.stanroy.todotaskmanager.domain.repository.TasksRepository
import org.koin.core.module.Module
import org.koin.dsl.module

object RepositoryModule {
    val provider: Module = module {
        single { provideTaskRepository(get()) }
    }

    private fun provideTaskRepository(tasksDao: TasksDao): TasksRepository {
        return TasksRepositoryImpl(tasksDao)
    }
}