package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult

class UpdateUserSessionUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(session: String, user: User): AppResult<Unit> {
        if (session.isEmpty()) {
            return AppResult.Error(AppError.Unknown)
        }
        val response = repository.postSession(session, user)
        val result = repository.updateSession(session, user)
        return if (response is AppResult.Success && result.isSuccess) {
            AppResult.Success(Unit)
        } else {
            AppResult.Error(AppError.Unknown)
        }
    }
}