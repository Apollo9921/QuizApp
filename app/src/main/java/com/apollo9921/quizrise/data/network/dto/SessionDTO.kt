package com.apollo9921.quizrise.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(
    val createdAt: String,
    val id: String,
    val remainingQuestions: Int,
    val totalQuestions: Int,
    val updatedAt: String
)