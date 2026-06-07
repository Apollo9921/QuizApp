package com.example.quizapp.domain.repository

import com.example.quizapp.domain.result.AppResult

interface GoogleAuthService {
    suspend fun getGoogleIdToken(): AppResult<String>
}