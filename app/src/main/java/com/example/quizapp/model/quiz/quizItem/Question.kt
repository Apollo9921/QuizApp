package com.example.quizapp.model.quiz.quizItem

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val text: String
)