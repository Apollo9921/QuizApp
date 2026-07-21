package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult

class PostUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
        isAnonymous: Boolean
    ): AppResult<Unit> {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return AppResult.Error(AppError.EmptyFields)
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            return AppResult.Error(AppError.InvalidEmailFormat)
        }
        if (password.length < 6) {
            return AppResult.Error(AppError.PasswordLength)
        }
        if (password != confirmPassword) {
            return AppResult.Error(AppError.PasswordMismatch)
        }
        return if (isAnonymous) {
            return repository.registerWithEmailByAnonymouslyAccount(email, password)
        } else {
            repository.registerWithEmail(email, password)
        }
    }
}