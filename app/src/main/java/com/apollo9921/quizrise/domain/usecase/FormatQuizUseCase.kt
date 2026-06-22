package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.data.network.dto.CloudQuizInputItem
import com.apollo9921.quizrise.data.network.dto.TranslatedQuizResult
import com.apollo9921.quizrise.domain.model.quiz.Quiz
import com.apollo9921.quizrise.domain.repository.CloudQuizTranslator
import com.apollo9921.quizrise.domain.result.AppResult

class FormatQuizUseCase(
    private val cloudQuizTranslator: CloudQuizTranslator
) {
    suspend operator fun invoke(data: List<Quiz>): AppResult<List<TranslatedQuizResult>> {
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