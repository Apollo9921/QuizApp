package com.example.quizapp.data.network.service

import com.example.quizapp.data.network.dto.QuizDTO

interface QuizService {
    suspend fun getQuiz(
        category: String,
        level: String,
        limit: Int = 5
    ): List<QuizDTO>
}