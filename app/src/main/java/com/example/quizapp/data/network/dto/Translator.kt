package com.example.quizapp.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TranslatedQuizResult(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)

@Keep
@Serializable
data class CloudQuizInputItem(
    val id: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)