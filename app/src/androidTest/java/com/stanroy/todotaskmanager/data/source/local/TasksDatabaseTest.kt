package com.stanroy.todotaskmanager.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.stanroy.todotaskmanager.data.source.local.dao.TasksDao
import com.stanroy.todotaskmanager.data.source.local.dto.SingleTaskDto
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
class TasksDatabaseTest {

    private lateinit var tasksDao: TasksDao
    private lateinit var tasksDatabase: TasksDatabase

    @Before
    fun setUp() {
        Timber.plant(Timber.DebugTree())
        val context = ApplicationProvider.getApplicationContext<Context>()
        tasksDatabase = Room.inMemoryDatabaseBuilder(context, TasksDatabase::class.java).build()
        tasksDao = tasksDatabase.tasksDao()
    }

    @After
    fun tearDown() {
        Timber.plant(Timber.DebugTree())
        tasksDatabase.close()
    }

    @Test
    fun createNewTaskConfirmCreation() = runBlocking {
        val singleTask =
            SingleTaskDto(1, "Buy Apples", System.currentTimeMillis(), "SHOPPING")

        tasksDao.newTask(singleTask)
        val taskById = tasksDao.findTaskById(1)

        Truth.assertThat(taskById).isEqualTo(singleTask)
    }

    @Test
    fun createMultipleTasksConfirmCreation() = runBlocking {
        val firstTask =
            SingleTaskDto(1, "Make breakfast", System.currentTimeMillis(), "PERSONAL")
        val secondTask =
            SingleTaskDto(2, "Go to the gym", System.currentTimeMillis(), "PERSONAL")
        val thirdTask =
            SingleTaskDto(3, "Buy groceries", System.currentTimeMillis(), "SHOPPING")

        tasksDao.newTask(firstTask)
        tasksDao.newTask(secondTask)
        tasksDao.newTask(thirdTask)
        val firstTaskById = tasksDao.findTaskById(1)
        val secondTaskById = tasksDao.findTaskById(2)
        val thirdTaskById = tasksDao.findTaskById(3)

        Truth.assertThat(firstTaskById).isEqualTo(firstTask)
        Truth.assertThat(secondTaskById).isEqualTo(secondTask)
        Truth.assertThat(thirdTaskById).isEqualTo(thirdTask)
    }

    @Test
    fun createMultipleTasksReturnTasksList() = runBlocking {
        val firstTask =
            SingleTaskDto(1, "Make breakfast", System.currentTimeMillis(), "PERSONAL")
        val secondTask =
            SingleTaskDto(2, "Go to the gym", System.currentTimeMillis(), "PERSONAL")
        val thirdTask =
            SingleTaskDto(3, "Buy groceries", System.currentTimeMillis(), "SHOPPING")

        tasksDao.newTask(firstTask)
        tasksDao.newTask(secondTask)
        tasksDao.newTask(thirdTask)

        val taskList = tasksDao.getAllTasks().take(1).toList()[0]

        Truth.assertThat(taskList).isNotEmpty()

        Timber.tag("TD_TEST").d("$taskList")
    }


    @Test
    fun createMultipleTasksRemoveTaskFromTheList() = runBlocking {
        val firstTask =
            SingleTaskDto(1, "Make breakfast", System.currentTimeMillis(), "PERSONAL")
        val secondTask =
            SingleTaskDto(2, "Go to the gym", System.currentTimeMillis(), "PERSONAL")

        tasksDao.newTask(firstTask)
        tasksDao.newTask(secondTask)


        val job = async {
            tasksDao.getAllTasks().take(1).toList()[0]
        }
        val outputList = job.await()
        Truth.assertThat(outputList.size).isEqualTo(2)

        tasksDao.deleteTask(firstTask)

        val secondJob = async {
            tasksDao.getAllTasks().take(1).toList()[0]
        }
        val modifiedTaskList = secondJob.await()
        Truth.assertThat(modifiedTaskList.size).isEqualTo(1)
    }


    @Test
    fun markTaskAsFinishedReturnFinishedTask() = runBlocking {
        val firstTask =
            SingleTaskDto(1, "Make breakfast", System.currentTimeMillis(), "PERSONAL")

        tasksDao.newTask(firstTask)
        tasksDao.markTaskAsFinished(firstTask.id)

        val finishedTask = tasksDao.findTaskById(firstTask.id)

        Truth.assertThat(finishedTask.taskIsFinished).isEqualTo(true)
    }

    @Test
    fun markTasksAsFinishedClearFinishedTasksReturnCurrentTaskList() = runBlocking {
        val firstTask =
            SingleTaskDto(1, "Make breakfast", System.currentTimeMillis(), "PERSONAL")
        val secondTask =
            SingleTaskDto(2, "Go to the gym", System.currentTimeMillis(), "PERSONAL")
        val thirdTask =
            SingleTaskDto(3, "Buy groceries", System.currentTimeMillis(), "SHOPPING")
        val fourthTask =
            SingleTaskDto(4, "Buy groceries", System.currentTimeMillis(), "SHOPPING")

        tasksDao.newTask(firstTask)
        tasksDao.newTask(secondTask)
        tasksDao.newTask(thirdTask)
        tasksDao.newTask(fourthTask)

        val job = async {
            tasksDao.getAllTasks().take(1).toList()[0]
        }
        val outputList = job.await()
        Truth.assertThat(outputList.size).isEqualTo(4)

        tasksDao.markTaskAsFinished(firstTask.id)
        tasksDao.markTaskAsFinished(fourthTask.id)

        tasksDao.removeFinishedTasks()

        val secondJob = async {
            tasksDao.getAllTasks().take(1).toList()[0]
        }
        val newList = secondJob.await()
        Truth.assertThat(newList.size).isEqualTo(2)
    }
}