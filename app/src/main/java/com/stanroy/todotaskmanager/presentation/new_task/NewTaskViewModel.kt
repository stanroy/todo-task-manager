package com.stanroy.todotaskmanager.presentation.new_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanroy.todotaskmanager.domain.usecase.AddNewTaskUseCase
import com.stanroy.todotaskmanager.presentation.utils.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class NewTaskViewModel(private val addNewTaskUseCase: AddNewTaskUseCase) : ViewModel() {


    val taskName = MutableLiveData<String>()
    val categorySelection = MutableLiveData<String>()

    private val cal = Calendar.getInstance()
    val day = MutableLiveData<Int>(cal.get(Calendar.DAY_OF_MONTH))
    val month = MutableLiveData<Int>(cal.get(Calendar.MONTH))
    val year = MutableLiveData<Int>(cal.get(Calendar.YEAR))
    val hour = MutableLiveData<Int>(cal.get(Calendar.HOUR_OF_DAY))
    val minute = MutableLiveData<Int>(cal.get(Calendar.MINUTE))

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    fun onAddTaskClicked() {
        validateTaskInput()
    }

    fun onAddRetry() {
        validateTaskInput()
    }

    private fun validateTaskInput() {
        val timeInMillis = dateToMilliseconds()

        if (taskName.value.isNullOrEmpty()) {
            _uiState.postValue(UIState.TASK_EMPTY)
        } else if (categorySelection.value.isNullOrEmpty()) {
            _uiState.postValue(UIState.CATEGORY_EMPTY)
        } else if (taskName.value.isNullOrEmpty() && categorySelection.value.isNullOrEmpty()) {
            _uiState.postValue(UIState.BOTH_EMPTY)
        }

        when {
            taskName.value.isNullOrEmpty() && categorySelection.value.isNullOrEmpty() -> _uiState.postValue(
                UIState.BOTH_EMPTY
            )
            taskName.value.isNullOrEmpty() -> _uiState.postValue(UIState.TASK_EMPTY)
            categorySelection.value.isNullOrEmpty() -> _uiState.postValue(UIState.CATEGORY_EMPTY)
            !taskName.value.isNullOrEmpty() && !categorySelection.value.isNullOrEmpty() && timeInMillis != 0L -> {
                _uiState.postValue(UIState.LOADING)
                addNewTask(timeInMillis)
            }
        }

    }

    private fun addNewTask(dueDate: Long) = viewModelScope.launch {
        try {
            val add = viewModelScope.async(Dispatchers.IO) {
                addNewTaskUseCase.execute(
                    taskName.value!!,
                    dueDate,
                    categorySelection.value!!,
                    false
                )
            }
            add.await()

            _uiState.postValue(UIState.STOP_LOADING)
            _uiState.postValue(UIState.SUCCESS)
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
            _uiState.postValue(UIState.STOP_LOADING)
            _uiState.postValue(UIState.FAILURE)
        }
    }

    private fun dateToMilliseconds(): Long {
        val cal = Calendar.getInstance()

        if (year.value != null && month.value != null && day.value != null && hour.value != null && minute.value != null) {
            cal.set(Calendar.YEAR, year.value!!)
            cal.set(Calendar.MONTH, month.value!!)
            cal.set(Calendar.DAY_OF_MONTH, day.value!!)
            cal.set(Calendar.HOUR_OF_DAY, hour.value!!)
            cal.set(Calendar.MINUTE, minute.value!!)
        }

        return cal.timeInMillis
    }


}