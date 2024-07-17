package com.example.remindernotes.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun register(user: User) = withContext(Dispatchers.IO) {
        userDao.register(user)
    }

    suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)
    }
    suspend fun getAllUsers(): List<User> = withContext(Dispatchers.IO) {
        userDao.getAllUsers()
    }
    suspend fun getUserById(id: Int): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(id)
    }
}