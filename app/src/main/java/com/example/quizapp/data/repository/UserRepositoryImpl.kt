package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.UserDAO
import com.example.quizapp.data.local.entity.UserEntity

class UserRepositoryImpl(private val userDAO: UserDAO) {

    suspend fun createUser(user: UserEntity) {
        userDAO.createUser(user)
    }

    fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String) {
        userDAO.updatePoints(totalPoints, totalPointsPossible, name)
    }

    fun updateBadge(badge: String, name: String) {
        userDAO.updateBadge(badge, name)
    }

}