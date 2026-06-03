package com.example.quizapp.domain.repository

import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.domain.result.AppResult

interface QuizRepository {
    suspend fun getQuiz(
        category: String,
        level: String,
        limit: Int = 5
    ): AppResult<List<QuizDTO>>
}