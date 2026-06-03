package com.example.quizapp.domain.usecase

import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.domain.repository.QuizRepository

class GetQuizUseCase(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(category: String, level: String) : List<QuizDTO> {
        return repository.getQuiz(category, level)
    }
}