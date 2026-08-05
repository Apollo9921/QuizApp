package com.apollo9921.quizrise.presentation.screens.quizResult

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.SaveQuizUseCase
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val saveQuizUseCase: SaveQuizUseCase,
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) : ViewModel() {

    var total: Int = 5
    var pointsReceived = mutableIntStateOf(0)
    var pointsToNextLevel = mutableIntStateOf(0)

    init {
        saveQuizProcess()
    }

    private fun saveQuizProcess() {
        viewModelScope.launch {
            try {
                val result = saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers)
                if (result is AppResult.Success) {
                    pointsReceived.intValue = result.data.first
                    pointsToNextLevel.intValue = result.data.second
                } else {

                }
            } catch (_: Exception) {

            }
        }
    }
}