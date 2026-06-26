package com.apollo9921.quizrise.domain.model.session

data class Session(
    val createdAt: String = "",
    val id: String = "",
    val remainingQuestions: Int = 0,
    val totalQuestions: Int = 0,
    val updatedAt: String = ""
)