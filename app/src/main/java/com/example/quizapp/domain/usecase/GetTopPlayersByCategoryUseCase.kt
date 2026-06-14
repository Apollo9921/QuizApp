package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.LeaderboardRepository

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