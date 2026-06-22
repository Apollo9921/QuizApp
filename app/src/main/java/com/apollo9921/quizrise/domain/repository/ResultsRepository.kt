package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.model.results.Results

interface ResultsRepository {
    suspend fun insertResults(results: Results)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int)
    suspend fun fetchResults(): Result<List<Results>>
}