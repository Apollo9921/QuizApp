package com.example.quizapp.model.user

class UserRepository(private val userDAO: UserDAO) {

    suspend fun createUser(user: User) {
        userDAO.createUser(user)
    }

    fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String) {
        userDAO.updatePoints(totalPoints, totalPointsPossible, name)
    }

    fun updateBadge(badge: String, name: String) {
        userDAO.updateBadge(badge, name)
    }

}