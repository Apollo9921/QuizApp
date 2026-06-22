package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.ResultsRepository

class UpdatePointsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(userName: String, pointsReceived: Int, pointsPossible: Int) {
        return repository.updatePoints(pointsReceived, pointsPossible, userName)
    }
}