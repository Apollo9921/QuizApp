package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.repository.ResultsRepository

class UpdateResultsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        return repository.updateResults(category, correctAnswers, incorrectAnswers)
    }
}