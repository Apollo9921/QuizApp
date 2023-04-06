package com.example.quizapp.model.results

class ResultsRepository(private val resultsDAO: ResultsDAO) {

    suspend fun createResults(results: Results) {
        resultsDAO.createResult(results)
    }

    fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        resultsDAO.updateResults(category, correctAnswers, incorrectAnswers)
    }

}