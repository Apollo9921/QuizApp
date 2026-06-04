package com.example.quizapp.domain.repository

import com.example.quizapp.data.local.entity.ResultsEntity

interface ResultsRepository {
    suspend fun createResults(results: ResultsEntity)
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)
    suspend fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int)
}