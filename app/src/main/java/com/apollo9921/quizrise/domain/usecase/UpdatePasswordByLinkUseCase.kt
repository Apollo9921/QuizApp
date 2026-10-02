package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult

class UpdatePasswordByLinkUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AppResult<Unit> {
        if (email.isEmpty()) {
            return AppResult.Error(AppError.EmptyFields)
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            return AppResult.Error(AppError.InvalidEmailFormat)
        }
        return authRepository.sendPasswordResetEmail(email)
    }
}