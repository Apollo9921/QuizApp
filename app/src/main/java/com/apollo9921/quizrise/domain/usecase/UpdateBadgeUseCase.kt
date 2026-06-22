package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.UserRepository

class UpdateBadgeUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(badge: String, name: String) {
        repository.updateBadge(badge, name)
    }
}