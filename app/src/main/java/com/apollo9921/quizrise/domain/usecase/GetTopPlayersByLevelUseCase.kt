package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.LeaderboardRepository

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