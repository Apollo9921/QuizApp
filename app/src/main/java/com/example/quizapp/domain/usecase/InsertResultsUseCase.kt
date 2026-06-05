package com.example.quizapp.domain.usecase

import android.content.Context
import com.example.quizapp.data.local.entity.ResultsEntity
import com.example.quizapp.domain.repository.ResultsRepository
import com.example.quizapp.presentation.utils.categories

class InsertResultsUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(context: Context) {
        for (i in categories.indices) {
            val results = ResultsEntity(
                category = context.resources.getString(categories[i]),
                correctAnswers = 0,
                incorrectAnswers = 0
            )
            repository.insertResults(results)
        }
    }
}