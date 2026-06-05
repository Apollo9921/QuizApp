package com.example.quizapp.domain.repository

interface GoogleAuthService {
    suspend fun getGoogleIdToken(): Result<String>
}