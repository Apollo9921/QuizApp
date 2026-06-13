package com.example.quizapp.data.mapper

import com.example.quizapp.data.local.entity.ResultsEntity
import com.example.quizapp.domain.model.results.Results

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