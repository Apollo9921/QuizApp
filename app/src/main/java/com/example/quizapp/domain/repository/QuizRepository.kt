package com.example.quizapp.domain.repository

import com.example.quizapp.data.network.dto.QuizDTO

interface QuizRepository {
    suspend fun getQuiz(
        category: String,
        level: String,
        limit: Int = 5
    ): List<QuizDTO>
}