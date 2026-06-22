package com.apollo9921.quizrise.data.repository

import com.apollo9921.quizrise.data.local.dao.ResultsDAO
import com.apollo9921.quizrise.data.local.dao.UserDAO
import com.apollo9921.quizrise.data.mapper.toResults
import com.apollo9921.quizrise.data.mapper.toResultsEntity
import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.repository.ResultsRepository

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