package com.apollo9921.quizrise.domain.model.quiz

data class Quiz(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: List<String>,
    val isNiche: Boolean,
    val question: Question,
    val regions: List<String?>,
    val tags: List<String>,
    val type: String
)