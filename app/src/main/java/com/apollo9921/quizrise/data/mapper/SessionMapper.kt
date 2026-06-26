package com.apollo9921.quizrise.data.mapper

import com.apollo9921.quizrise.data.network.dto.SessionDTO
import com.apollo9921.quizrise.domain.model.session.Session

fun SessionDTO.toSession(): Session {
    return Session(
        createdAt = createdAt,
        id = id,
        remainingQuestions = remainingQuestions,
        totalQuestions = totalQuestions,
        updatedAt = updatedAt
    )
}

fun Session.toSessionDTO(): SessionDTO {
    return SessionDTO(
        createdAt = createdAt,
        id = id,
        remainingQuestions = remainingQuestions,
        totalQuestions = totalQuestions,
        updatedAt = updatedAt
    )
}