package com.stanroy.todotaskmanager.presentation.tasks_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanroy.todotaskmanager.domain.model.SingleTask
import com.stanroy.todotaskmanager.domain.usecase.GetAllTasksUseCase
import com.stanroy.todotaskmanager.domain.usecase.MarkTaskAsFinishedUseCase
import com.stanroy.todotaskmanager.domain.usecase.RemoveFinishedTasksUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class TasksListViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val removeFinishedTasksUseCase: RemoveFinishedTasksUseCase,
    private val markTaskAsFinishedUseCase: MarkTaskAsFinishedUseCase
) : ViewModel() {

    private val _currentTaskList = MutableLiveData<List<SingleTask>>(listOf())
    val currentTaskList: LiveData<List<SingleTask>>
        get() = _currentTaskList
    private val _singleItemChanged = MutableLiveData<Boolean>()
    val singleItemChanged: LiveData<Boolean>
        get() = _singleItemChanged

    init {
        retrieveTasksFromDatabase()
    }

    fun onTaskFinished(task: SingleTask) {
        markTaskAsFinished(task)
    }

    fun onNewTaskAdded() {
        _currentTaskList.postValue(emptyList())
        retrieveTasksFromDatabase()
    }

    fun onClearTasksClicked() {
        clearFinishedTasks()
    }

    private fun retrieveTasksFromDatabase() {
        viewModelScope.launch {
            getAllTasksUseCase.execute().collect {
                _currentTaskList.postValue(it)
            }
        }
    }

    private fun markTaskAsFinished(task: SingleTask) = viewModelScope.launch {
        markTaskAsFinishedUseCase.execute(task.id)
        _singleItemChanged.postValue(true)
    }

    private fun clearFinishedTasks() = viewModelScope.launch {

        Timber.tag("ListFrag").d("Removing finished tasks")
        val removeTasks = async { removeFinishedTasksUseCase.execute() }
        removeTasks.await()
        _currentTaskList.postValue(emptyList())
        retrieveTasksFromDatabase()

    }


}