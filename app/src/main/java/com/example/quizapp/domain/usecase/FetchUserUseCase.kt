package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.domain.repository.UserRepository

class FetchUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<UserEntity> {
        return repository.fetchUser()
    }
}