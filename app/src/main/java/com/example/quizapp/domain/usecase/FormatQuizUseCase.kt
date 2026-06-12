package com.example.quizapp.domain.usecase

import com.example.quizapp.data.network.dto.CloudQuizInputItem
import com.example.quizapp.data.network.dto.TranslatedQuizResult
import com.example.quizapp.domain.model.quiz.Quiz
import com.example.quizapp.domain.repository.CloudQuizTranslator

class FormatQuizUseCase(
    private val cloudQuizTranslator: CloudQuizTranslator
) {
    suspend operator fun invoke(data: List<Quiz>): List<TranslatedQuizResult> {
        val inputBlock = data.map { q ->
            CloudQuizInputItem(
                id = q.id.toString(),
                question = q.question.text,
                correctAnswer = q.correctAnswer,
                incorrectAnswers = q.incorrectAnswers
            )
        }
        return cloudQuizTranslator.translateQuizBlock(inputBlock)
    }
}