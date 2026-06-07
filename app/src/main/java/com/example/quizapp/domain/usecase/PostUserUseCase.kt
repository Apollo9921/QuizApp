package com.example.quizapp.domain.usecase

import com.example.quizapp.R
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.result.AppResult

class PostUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
    ): AppResult<Unit> {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return AppResult.Error(R.string.empty_fields)
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            return AppResult.Error(R.string.invalid_email_format)
        }
        if (password.length < 6) {
            return AppResult.Error(R.string.password_length)
        }
        if (password != confirmPassword) {
            return AppResult.Error(R.string.password_mismatch)
        }
        return repository.registerWithEmail(email, password)
    }
}