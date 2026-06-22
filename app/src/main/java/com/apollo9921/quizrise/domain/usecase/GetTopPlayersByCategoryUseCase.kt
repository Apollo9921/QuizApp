package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.repository.LeaderboardRepository

class GetTopPlayersByCategoryUseCase(
    private val leaderboardRepository: LeaderboardRepository
) {
    suspend operator fun invoke(
        category: String,
        onSuccess: (List<Results>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        return leaderboardRepository.getTopPlayersByCategory(category, onSuccess, onFailure)
    }
}