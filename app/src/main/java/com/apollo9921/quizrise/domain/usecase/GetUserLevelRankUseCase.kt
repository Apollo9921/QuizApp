package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.UserRepository

class GetUserLevelRankUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        badge: String,
        totalPoints: Int,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
       return userRepository.getUserLevelRank(badge, totalPoints, onSuccess, onFailure)
    }
}