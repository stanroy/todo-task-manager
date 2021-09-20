package com.stanroy.todotaskmanager.di

import com.stanroy.todotaskmanager.domain.repository.TasksRepository
import com.stanroy.todotaskmanager.domain.usecase.AddNewTaskUseCase
import com.stanroy.todotaskmanager.domain.usecase.GetAllTasksUseCase
import com.stanroy.todotaskmanager.domain.usecase.MarkTaskAsFinishedUseCase
import com.stanroy.todotaskmanager.domain.usecase.RemoveFinishedTasksUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object UseCaseModule {
    val provider: Module = module {
        single { provideGetAllTasksUseCase(get()) }
        single { provideMarkAsFinishedUseCase(get()) }
        single { provideAddNewTaskUseCase(get()) }
        single { provideRemoveFinishedTasksUseCase(get()) }
    }

    private fun provideRemoveFinishedTasksUseCase(repository: TasksRepository): RemoveFinishedTasksUseCase {
        return RemoveFinishedTasksUseCase(repository)
    }

    private fun provideGetAllTasksUseCase(repository: TasksRepository): GetAllTasksUseCase {
        return GetAllTasksUseCase(repository)
    }

    private fun provideAddNewTaskUseCase(repository: TasksRepository): AddNewTaskUseCase {
        return AddNewTaskUseCase(repository)
    }

    private fun provideMarkAsFinishedUseCase(repository: TasksRepository): MarkTaskAsFinishedUseCase {
        return MarkTaskAsFinishedUseCase(repository)
    }

}