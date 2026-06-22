package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.result.AppResult

interface AuthRepository {
    suspend fun registerWithEmail(email: String, password: String): AppResult<Unit>
    suspend fun signInWithGoogle(idToken: String): AppResult<Unit>
    suspend fun loginWithEmail(email: String, password: String): AppResult<Unit>
    suspend fun checkIfUserExists(): AppResult<Boolean>
}