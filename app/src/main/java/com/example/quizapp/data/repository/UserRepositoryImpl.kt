package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.UserDAO
import com.example.quizapp.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userDAO: UserDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchUser(): Result<UserEntity> {
        return withContext(ioDispatcher) {
            try {
                val result = userDAO.fetchUser()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createUser(user: UserEntity) {
        withContext(ioDispatcher) {
            userDAO.insertUser(user)
        }
    }

    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String) {
        withContext(ioDispatcher) {
            userDAO.updatePoints(totalPoints, totalPointsPossible, name)
        }
    }

    suspend fun updateBadge(badge: String, name: String) {
        withContext(ioDispatcher) {
            userDAO.updateBadge(badge, name)
        }
    }

}