package com.apollo9921.quizrise.presentation.screens.quizResult

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.CalculateQuizResultUseCase
import com.apollo9921.quizrise.domain.usecase.GetPlayerAheadByCategoryUseCase
import com.apollo9921.quizrise.domain.usecase.GetPlayerBehindByCategoryUseCase
import com.apollo9921.quizrise.domain.usecase.SaveQuizUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val calculateQuizResultUseCase: CalculateQuizResultUseCase,
    private val saveQuizUseCase: SaveQuizUseCase,
    private val getPlayerAheadByCategoryUseCase: GetPlayerAheadByCategoryUseCase,
    private val getPlayerBehindByCategoryUseCase: GetPlayerBehindByCategoryUseCase,
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    var total: Int = 5

    data class QuizResultUiState(
        val userNameAheadOrBehind: String = "",
        val totalPointsAhead: Int = 0,
        val totalPointsBehind: Int = 0
    )

    sealed class UIState {
        object Idle : UIState()
        data class Success(
            val pointsReceived: Int = 0,
            val pointsToNextLevel: Int = 0,
            val quizResultUiState: QuizResultUiState = QuizResultUiState()
        ) : UIState()
        data class Error(val message: Int) : UIState()
    }

    init {
        saveQuizProcess()
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
                    getPlayerAhead()
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

    private fun getPlayerAhead() {
        viewModelScope.launch {
            getPlayerAheadByCategoryUseCase.invoke(
                selectedCategory = category,
                onSuccess = { result, totalPointsOfCurrentUser ->
                    if (result != null && totalPointsOfCurrentUser != null) {
                        val totalPointsOfAheadUser = result.correctAnswers * 5
                        val difference = totalPointsOfAheadUser - totalPointsOfCurrentUser
                        _uiState.value = UIState.Success(
                            quizResultUiState = QuizResultUiState(
                                userNameAheadOrBehind = result.username,
                                totalPointsBehind = difference
                            )
                        )
                    } else {
                        getPlayerBehind()
                    }
                },
                onFailure = {
                    Log.e("QuizResultViewModel", "Failed to fetch player ahead ${it.message}")
                }
            )
        }
    }

    private fun getPlayerBehind() {
        viewModelScope.launch {
            getPlayerBehindByCategoryUseCase.invoke(
                selectedCategory = category,
                onSuccess = { result, totalPointsOfCurrentUser ->
                    if (result != null && totalPointsOfCurrentUser != null) {
                        val totalPointsOfBehindUser = result.correctAnswers * 5
                        val difference = totalPointsOfCurrentUser - totalPointsOfBehindUser
                        _uiState.value = UIState.Success(
                            quizResultUiState = QuizResultUiState(
                                userNameAheadOrBehind = result.username,
                                totalPointsAhead = difference
                            )
                        )
                    }
                },
                onFailure = {
                    Log.e("QuizResultViewModel", "Failed to fetch player behind ${it.message}")
                }
            )
        }
    }
}