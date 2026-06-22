package com.apollo9921.quizrise.data.mapper

import com.apollo9921.quizrise.data.network.dto.QuestionDTO
import com.apollo9921.quizrise.data.network.dto.QuizDTO
import com.apollo9921.quizrise.domain.model.quiz.Question
import com.apollo9921.quizrise.domain.model.quiz.Quiz

fun QuizDTO.toQuiz(): Quiz {
    return Quiz(
        category = category,
        correctAnswer = correctAnswer,
        difficulty = difficulty,
        id = id,
        incorrectAnswers = incorrectAnswers,
        isNiche = isNiche,
        regions = regions,
        tags = tags,
        type = type,
        question = question.toQuestion()
    )
}

private fun QuestionDTO.toQuestion(): Question {
    return Question(
        text = text
    )
}