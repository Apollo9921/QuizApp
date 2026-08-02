package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.result.AppResult

interface UserRepository {
    suspend fun fetchUser(): Result<User>
    suspend fun insertUser(user: User)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateBadge(badge: String, name: String)
    suspend fun postUserAndResults(user: User, results: List<Results>): AppResult<Unit>
    suspend fun updateUserAndResults(user: User, results: Results): AppResult<Unit>
    suspend fun getUser(): AppResult<User>
    suspend fun getResults(): AppResult<List<Results>>
    suspend fun clearAllData()
    suspend fun deleteAccount(): AppResult<Unit>
    suspend fun postSession(session: String, user: User): AppResult<Unit>
    suspend fun updateSession(session: String, user: User) : Result<Unit>
    suspend fun updateName(name: String, oldName: String) : Result<Unit>
    suspend fun postUserName(name: String, results: List<Results>) : AppResult<Unit>
}