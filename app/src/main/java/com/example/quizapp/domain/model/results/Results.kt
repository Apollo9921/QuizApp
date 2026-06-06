package com.example.quizapp.domain.model.results

import androidx.annotation.Keep

@Keep
data class Results(
    val category: String = "",
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0
)