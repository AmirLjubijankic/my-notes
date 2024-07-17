package com.example.remindernotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.remindernotes.data.TaskDao
import com.example.remindernotes.data.UserDao


class TaskViewModelFactory (private val taskDao: TaskDao, private val userDao: UserDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao, userDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}