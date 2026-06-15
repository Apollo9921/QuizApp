package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository

class InsertUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.insertUser(user)
    }
}