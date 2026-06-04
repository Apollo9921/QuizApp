package com.example.quizapp.presentation.screens.quizResult

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.usecase.UpdatePointsUseCase
import com.example.quizapp.domain.usecase.UpdateResultsUseCase
import com.example.quizapp.presentation.userManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val updateResultsUseCase: UpdateResultsUseCase,
    private val updatePointsUseCase: UpdatePointsUseCase,
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) : ViewModel() {

    private var userName: String = ""
    private val pointsPossible: Int = 25

    var total: Int = 5
    var pointsReceived = mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            userName = userManager.userName.first().toString()
            pointsReceived.intValue = correctAnswers * 5
            updateResults()
            updatePoints()
        }
    }

    private fun updateResults() {
        viewModelScope.launch {
            updateResultsUseCase.invoke(
                category = category,
                correctAnswers = correctAnswers,
                incorrectAnswers = incorrectAnswers
            )
        }
    }

    private fun updatePoints() {
        viewModelScope.launch {
            updatePointsUseCase.invoke(
                userName = userName,
                pointsReceived = pointsReceived.intValue,
                pointsPossible = pointsPossible
            )
        }
    }
}