package com.example.quizapp.data.mapper

import com.example.quizapp.data.network.dto.QuestionDTO
import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.domain.model.quiz.Question
import com.example.quizapp.domain.model.quiz.Quiz

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