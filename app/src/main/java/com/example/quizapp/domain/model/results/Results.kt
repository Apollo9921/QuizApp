package com.example.quizapp.domain.model.results

data class Results(
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
)