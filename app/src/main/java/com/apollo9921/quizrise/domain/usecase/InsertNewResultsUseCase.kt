package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.util.QuizCategory

class InsertNewResultsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(userName: String) {
        for (i in QuizCategory.entries.indices) {
            val results = Results(
                category = QuizCategory.entries[i].categoryName,
                correctAnswers = 0,
                incorrectAnswers = 0,
                username = userName
            )
            repository.insertResults(results)
        }
    }
}