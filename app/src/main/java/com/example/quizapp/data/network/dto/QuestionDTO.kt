package com.example.quizapp.data.network.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class QuestionDTO(
    val text: String
)