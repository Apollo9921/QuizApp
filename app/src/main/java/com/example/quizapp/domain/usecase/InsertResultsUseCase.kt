package com.example.quizapp.domain.usecase

import android.content.Context
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.ResultsRepository
import com.example.quizapp.presentation.utils.categories

class InsertResultsUseCase(
    private val context: Context,
    private val repository: ResultsRepository
) {
    suspend operator fun invoke() {
        for (i in categories.indices) {
            val results = Results(
                category = context.resources.getString(categories[i]),
                correctAnswers = 0,
                incorrectAnswers = 0
            )
            repository.insertResults(results)
        }
    }
}