package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.repository.ResultsRepository

class UpdatePointsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(userName: String, pointsReceived: Int, pointsPossible: Int) {
        return repository.updatePoints(pointsReceived, pointsPossible, userName)
    }
}