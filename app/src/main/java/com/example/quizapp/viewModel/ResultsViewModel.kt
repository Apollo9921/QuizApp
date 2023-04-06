package com.example.quizapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.database.QuizDatabase
import com.example.quizapp.model.results.Results
import com.example.quizapp.model.results.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultsViewModel(application: Context): ViewModel() {

    private val repository: ResultsRepository

    init {
        val resultsDAO = QuizDatabase.getDatabase(application).resultsDao()
        repository = ResultsRepository(resultsDAO)
    }

    fun createCategory(results: Results) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createResults(results)
        }
    }

    fun updateCategory(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateResults(category, correctAnswers, incorrectAnswers)
        }
    }

}