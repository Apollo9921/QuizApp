package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.result.AppResult

interface UserRepository {
    suspend fun fetchUser(): Result<User>
    suspend fun insertUser(user: User)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateBadge(badge: String, name: String)
    suspend fun postUserAndResults(user: User, results: List<Results>): AppResult<Unit>
    suspend fun updateUserAndResults(user: User, results: Results): AppResult<Unit>
    suspend fun getUser(): AppResult<User>
    suspend fun getResults(): AppResult<List<Results>>
}