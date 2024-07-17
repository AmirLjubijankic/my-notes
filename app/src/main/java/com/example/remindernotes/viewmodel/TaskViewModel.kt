package com.example.remindernotes.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindernotes.data.Task
import com.example.remindernotes.data.TaskDao
import com.example.remindernotes.data.UserDao
import kotlinx.coroutines.launch
import java.time.YearMonth

class TaskViewModel(private val taskDao: TaskDao, private val userDao: UserDao): ViewModel(){
    val tasks = mutableStateListOf<Task>()

    init {
        viewModelScope.launch {
            tasks.addAll(taskDao.getAll())
        }
    }

    fun addTask(task: Task){

        viewModelScope.launch {
            taskDao.insertTask(task)
            tasks.add(task)
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
            }
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            tasks.remove(task)
        }
    }
    suspend fun getTasksForUser(userId: Int): List<Task> {
        return taskDao.getTasksForUser(userId)
    }
    suspend fun getTasksForUserByMonth(userId: Int, yearMonth: YearMonth): List<Task> {
        val tasksForUser = taskDao.getTasksForUser(userId)
        return tasksForUser.filter {
            it.dueDate.year == yearMonth.year && it.dueDate.monthValue == yearMonth.monthValue
        }
    }

    fun sortTasks(tasks: List<Task>): List<Task> {
        return tasks.sortedWith(compareBy({ it.dueDate }, { it.dueTime }))
    }

}