package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.ResultsDAO
import com.example.quizapp.data.local.dao.UserDAO
import com.example.quizapp.data.local.entity.ResultsEntity
import com.example.quizapp.domain.repository.ResultsRepository

class ResultsRepositoryImpl(
    private val resultsDAO: ResultsDAO,
    private val userDAO: UserDAO
) : ResultsRepository {

    override suspend fun createResults(results: ResultsEntity) {
        resultsDAO.createResult(results)
    }

    override suspend fun updatePoints(
        totalPoints: Int,
        totalPointsPossible: Int,
        name: String
    ) {
        userDAO.updatePoints(totalPoints, totalPointsPossible, name)
    }

    override suspend fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        resultsDAO.updateResults(category, correctAnswers, incorrectAnswers)
    }

}