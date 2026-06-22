package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.repository.ResultsRepository

class InsertResultsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(result: Results) {
        repository.insertResults(result)
    }
}