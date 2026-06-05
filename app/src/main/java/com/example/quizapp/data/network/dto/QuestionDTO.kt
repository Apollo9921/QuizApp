package com.example.quizapp.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO(
    val text: String
)