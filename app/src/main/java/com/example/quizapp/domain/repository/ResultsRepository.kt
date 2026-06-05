package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.results.Results

interface ResultsRepository {
    suspend fun insertResults(results: Results)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int)
}