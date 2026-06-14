package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.LeaderboardRepository

class GetTopPlayersByLevelUseCase(
    private val leaderboardRepository: LeaderboardRepository
) {
    suspend operator fun invoke(
        badge: String,
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        return leaderboardRepository.getTopPlayersByLevel(badge, onSuccess, onFailure)
    }
}