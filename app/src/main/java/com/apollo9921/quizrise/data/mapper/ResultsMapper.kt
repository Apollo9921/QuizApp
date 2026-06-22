package com.apollo9921.quizrise.data.mapper

import com.apollo9921.quizrise.data.local.entity.ResultsEntity
import com.apollo9921.quizrise.domain.model.results.Results

fun ResultsEntity.toResults(): Results {
    return Results(
        category = category,
        correctAnswers = correctAnswers,
        incorrectAnswers = incorrectAnswers
    )
}

fun Results.toResultsEntity(): ResultsEntity {
    return ResultsEntity(
        category = category,
        correctAnswers = correctAnswers,
        incorrectAnswers = incorrectAnswers,
        username = username
    )
}