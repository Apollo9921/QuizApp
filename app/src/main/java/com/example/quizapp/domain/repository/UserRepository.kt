package com.example.quizapp.domain.repository

import com.example.quizapp.data.local.entity.UserEntity

interface UserRepository {
    suspend fun fetchUser(): Result<UserEntity>
    suspend fun createUser(user: UserEntity)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateBadge(badge: String, name: String)
}