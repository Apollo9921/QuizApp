package com.example.quizapp.domain.repository

interface AuthRepository {
    suspend fun registerWithEmail(email: String, password: String): Result<Unit>
    suspend fun registerWithGoogle(idToken: String): Result<Unit>
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
}