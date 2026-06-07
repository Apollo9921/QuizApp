package com.example.quizapp.domain.repository

import com.example.quizapp.domain.result.AppResult

interface AuthRepository {
    suspend fun registerWithEmail(email: String, password: String): AppResult<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
    suspend fun checkIfUserExists(): Result<Boolean>
}