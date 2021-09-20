package com.stanroy.todotaskmanager.di

import com.stanroy.todotaskmanager.domain.usecase.AddNewTaskUseCase
import com.stanroy.todotaskmanager.domain.usecase.GetAllTasksUseCase
import com.stanroy.todotaskmanager.domain.usecase.MarkTaskAsFinishedUseCase
import com.stanroy.todotaskmanager.domain.usecase.RemoveFinishedTasksUseCase
import com.stanroy.todotaskmanager.presentation.new_task.NewTaskViewModel
import com.stanroy.todotaskmanager.presentation.tasks_list.TasksListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


object ViewModelModule {
    val provider: Module = module {
        viewModel { provideTasksListViewModel(get(), get(), get()) }
        viewModel { provideNewTaskViewModel(get()) }
    }

    private fun provideTasksListViewModel(
        getAllTasksUseCase: GetAllTasksUseCase,
        removeFinishedTasksUseCase: RemoveFinishedTasksUseCase,
        markTaskAsFinishedUseCase: MarkTaskAsFinishedUseCase
    ): TasksListViewModel {
        return TasksListViewModel(
            getAllTasksUseCase,
            removeFinishedTasksUseCase,
            markTaskAsFinishedUseCase
        )
    }

    private fun provideNewTaskViewModel(addNewTaskUseCase: AddNewTaskUseCase): NewTaskViewModel {
        return NewTaskViewModel(addNewTaskUseCase)
    }
}