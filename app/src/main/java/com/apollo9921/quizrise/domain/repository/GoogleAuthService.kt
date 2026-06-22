package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.result.AppResult

interface GoogleAuthService {
    suspend fun getGoogleIdToken(): AppResult<String>
}