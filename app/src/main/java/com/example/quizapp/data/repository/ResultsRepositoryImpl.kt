package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.ResultsDAO
import com.example.quizapp.data.local.entity.ResultsEntity

class ResultsRepositoryImpl(private val resultsDAO: ResultsDAO) {

    suspend fun createResults(results: ResultsEntity) {
        resultsDAO.createResult(results)
    }

    fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        resultsDAO.updateResults(category, correctAnswers, incorrectAnswers)
    }

}