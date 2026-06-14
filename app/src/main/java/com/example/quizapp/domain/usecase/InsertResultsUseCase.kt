package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.ResultsRepository
import com.example.quizapp.domain.util.QuizCategory

class InsertResultsUseCase(
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