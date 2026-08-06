package com.apollo9921.quizrise.presentation.screens.quizResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.CalculateQuizResultUseCase
import com.apollo9921.quizrise.domain.usecase.SaveQuizUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val calculateQuizResultUseCase: CalculateQuizResultUseCase,
    private val saveQuizUseCase: SaveQuizUseCase,
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    var total: Int = 5

    init {
        saveQuizProcess()
    }

    sealed class UIState {
        object Idle : UIState()
        data class Success(val pointsReceived: Int, val pointsToNextLevel: Int) : UIState()
        data class Error(val message: Int) : UIState()
    }

    fun saveQuizProcess() {
        viewModelScope.launch {
            try {
                _uiState.value = UIState.Idle
                val calculateResponse = calculateQuizResultUseCase.invoke(correctAnswers)
                if (calculateResponse is AppResult.Success) {
                    _uiState.value =
                        UIState.Success(
                            pointsReceived = calculateResponse.data.first,
                            pointsToNextLevel = calculateResponse.data.second
                        )
                } else {
                    _uiState.value = UIState.Error(message = R.string.unexpected_error)
                }
                val result = saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers)
                if (result is AppResult.Error) {
                    _uiState.value = UIState.Error(message = R.string.unexpected_error)
                }

            } catch (_: Exception) {
                _uiState.value = UIState.Error(message = R.string.unexpected_error)
            }
        }
    }
}