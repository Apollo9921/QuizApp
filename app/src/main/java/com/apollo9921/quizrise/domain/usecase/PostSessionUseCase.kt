package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.session.Session
import com.apollo9921.quizrise.domain.repository.QuizRepository
import com.apollo9921.quizrise.domain.result.AppResult

class PostSessionUseCase(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(): AppResult<Session> {
        return repository.createSession()
    }
}