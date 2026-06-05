package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.UserDAO
import com.example.quizapp.data.mapper.toUser
import com.example.quizapp.data.mapper.toUserEntity
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userDAO: UserDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun fetchUser(): Result<User> {
        return withContext(ioDispatcher) {
            try {
                val result = userDAO.fetchUser()
                Result.success(result.toUser())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun insertUser(user: User) {
        withContext(ioDispatcher) {
            userDAO.insertUser(user.toUserEntity())
        }
    }

    override suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String) {
        withContext(ioDispatcher) {
            userDAO.updatePoints(totalPoints, totalPointsPossible, name)
        }
    }

    override suspend fun updateBadge(badge: String, name: String) {
        withContext(ioDispatcher) {
            userDAO.updateBadge(badge, name)
        }
    }

}