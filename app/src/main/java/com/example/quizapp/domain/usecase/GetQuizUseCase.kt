package com.example.quizapp.domain.usecase

import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.domain.repository.QuizRepository
import com.example.quizapp.domain.result.AppResult

class GetQuizUseCase(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(category: String, level: String) : AppResult<List<QuizDTO>> {
        return if (category.isEmpty() || level.isEmpty()) {
            AppResult.Error("No category or level defined")
        } else {
            repository.getQuiz(category, level)
        }
    }
}