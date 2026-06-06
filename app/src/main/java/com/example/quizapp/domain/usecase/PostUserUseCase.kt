package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.repository.AuthRepository

class PostUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        //TODO SAVE ERROR MESSAGES ON STRINGS.XML
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return Result.failure(Exception("There are empty fields"))
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            return Result.failure(Exception("Invalid Email Format"))
        }
        if (password.length < 6) {
            return Result.failure(Exception("Password must contain at least 6 characters"))
        }
        if (password != confirmPassword) {
            return Result.failure(Exception("Passwords do not match"))
        }
        return repository.registerWithEmail(email, password)
    }
}