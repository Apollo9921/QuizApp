package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.repository.UserRepository

class UpdateBadgeUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(badge: String, name: String) {
        repository.updateBadge(badge, name)
    }
}