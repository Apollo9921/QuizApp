package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.user.User

interface UserRepository {
    suspend fun fetchUser(): Result<User>
    suspend fun insertUser(user: User)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateBadge(badge: String, name: String)
}