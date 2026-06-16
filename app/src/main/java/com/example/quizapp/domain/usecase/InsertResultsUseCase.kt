package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.ResultsRepository

class InsertResultsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(result: Results) {
        repository.insertResults(result)
    }
}