package com.example.quizapp.model.quiz


@kotlinx.serialization.Serializable
data class QuizItem(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: List<String>,
    val isNiche: Boolean,
    val question: String,
    val regions: List<String?>,
    val tags: List<String>,
    val type: String
)