package com.example.quizapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.local.entity.ResultsEntity
import com.example.quizapp.data.repository.ResultsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultsViewModel(application: Context): ViewModel() {

    private val repository: ResultsRepositoryImpl

    init {
        val resultsDAO = QuizDatabase.getDatabase(application).resultsDao()
        val userDAO = QuizDatabase.getDatabase(application).userDao()
        repository = ResultsRepositoryImpl(resultsDAO, userDAO)
    }

    fun createCategory(results: ResultsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createResults(results)
        }
    }

}