package com.example.quizapp.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizDTO(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: List<String>,
    val isNiche: Boolean,
    val question: QuestionDTO,
    val regions: List<String?>,
    val tags: List<String>,
    val type: String
)