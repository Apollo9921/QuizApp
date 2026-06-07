package com.example.quizapp.domain.usecase

import com.example.quizapp.R
import com.example.quizapp.domain.model.quiz.Quiz
import com.example.quizapp.domain.repository.QuizRepository
import com.example.quizapp.domain.result.AppResult

class GetQuizUseCase(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(category: String, level: String) : AppResult<List<Quiz>> {
        return if (category.isEmpty() || level.isEmpty()) {
            AppResult.Error(R.string.no_category_or_level_defined)
        } else {
            repository.getQuiz(category, level)
        }
    }
}