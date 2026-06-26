package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.model.quiz.Quiz
import com.apollo9921.quizrise.domain.model.session.Session
import com.apollo9921.quizrise.domain.result.AppResult

interface QuizRepository {
    suspend fun getQuiz(
        category: String,
        level: String,
        session: String,
        limit: Int = 5
    ): AppResult<List<Quiz>>

    suspend fun createSession(): AppResult<Session>
}