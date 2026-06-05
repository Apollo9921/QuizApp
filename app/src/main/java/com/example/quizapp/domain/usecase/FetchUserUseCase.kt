package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository

class FetchUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.fetchUser()
    }
}