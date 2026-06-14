package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.ResultsRepository

class FetchResultsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(): Result<List<Results>> {
        return repository.fetchResults()
    }
}