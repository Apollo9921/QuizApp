package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.ResultsDAO
import com.example.quizapp.data.local.dao.UserDAO
import com.example.quizapp.data.mapper.toResults
import com.example.quizapp.data.mapper.toResultsEntity
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.ResultsRepository

class ResultsRepositoryImpl(
    private val resultsDAO: ResultsDAO,
    private val userDAO: UserDAO
) : ResultsRepository {

    override suspend fun insertResults(results: Results) {
        resultsDAO.createResult(results.toResultsEntity())
    }

    override suspend fun updatePoints(
        totalPoints: Int,
        totalPointsPossible: Int,
        name: String
    ) {
        userDAO.updatePoints(totalPoints, totalPointsPossible, name)
    }

    override suspend fun updateResults(
        category: String,
        correctAnswers: Int,
        incorrectAnswers: Int
    ) {
        resultsDAO.updateResults(category, correctAnswers, incorrectAnswers)
    }

    override suspend fun fetchResults(): Result<List<Results>> {
        return try {
            val result = resultsDAO.fetchResults()
            Result.success(result.map { it.toResults() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}