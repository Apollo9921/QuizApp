package com.apollo9921.quizrise.presentation.screens.quizResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.CalculateQuizResultUseCase
import com.apollo9921.quizrise.domain.usecase.GetPlayerAheadByCategoryUseCase
import com.apollo9921.quizrise.domain.usecase.GetPlayerBehindByCategoryUseCase
import com.apollo9921.quizrise.domain.usecase.SaveQuizUseCase
import com.apollo9921.quizrise.presentation.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val calculateQuizResultUseCase: CalculateQuizResultUseCase,
    private val saveQuizUseCase: SaveQuizUseCase,
    private val getPlayerAheadByCategoryUseCase: GetPlayerAheadByCategoryUseCase,
    private val getPlayerBehindByCategoryUseCase: GetPlayerBehindByCategoryUseCase,
    val level: String,
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
                    val result = saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers)
                    if (result is AppResult.Error) {
                        _uiState.value = UIState.Error(message = R.string.unexpected_error)
                    } else {
                        getPlayerAhead(calculateResponse)
                    }
                } else {
                    _uiState.value = UIState.Error(message = R.string.unexpected_error)
                }
            } catch (_: Exception) {
                _uiState.value = UIState.Error(message = R.string.unexpected_error)
            }
        }
    }

    private fun getPlayerAhead(calculateResponse: AppResult.Success<Pair<Int, Int>>) {
        viewModelScope.launch {
            getPlayerAheadByCategoryUseCase.invoke(
                selectedCategory = category,
                onSuccess = { result, totalPointsOfCurrentUser ->
                    if (result != null && totalPointsOfCurrentUser != null) {
                        val totalPointsOfAheadUser = result.correctAnswers * 5
                        val difference = totalPointsOfAheadUser - totalPointsOfCurrentUser
                        _uiState.value = UIState.Success(
                            pointsReceived = calculateResponse.data.first,
                            pointsToNextLevel = calculateResponse.data.second,
                            quizResultUiState = QuizResultUiState(
                                userNameAheadOrBehind = result.username,
                                totalPointsBehind = difference
                            )
                        )
                    } else {
                        getPlayerBehind(calculateResponse)
                    }
                },
                onFailure = {
                    _uiState.value =
                        UIState.Success(
                            pointsReceived = calculateResponse.data.first,
                            pointsToNextLevel = calculateResponse.data.second
                        )
                }
            )
        }
    }

    private fun getPlayerBehind(calculateResponse: AppResult.Success<Pair<Int, Int>>) {
        viewModelScope.launch {
            getPlayerBehindByCategoryUseCase.invoke(
                selectedCategory = category,
                onSuccess = { result, totalPointsOfCurrentUser ->
                    if (result != null && totalPointsOfCurrentUser != null) {
                        val totalPointsOfBehindUser = result.correctAnswers * 5
                        val difference = totalPointsOfCurrentUser - totalPointsOfBehindUser
                        _uiState.value = UIState.Success(
                            pointsReceived = calculateResponse.data.first,
                            pointsToNextLevel = calculateResponse.data.second,
                            quizResultUiState = QuizResultUiState(
                                userNameAheadOrBehind = result.username,
                                totalPointsAhead = difference
                            )
                        )
                    }
                },
                onFailure = {
                    _uiState.value =
                        UIState.Success(
                            pointsReceived = calculateResponse.data.first,
                            pointsToNextLevel = calculateResponse.data.second
                        )
                }
            )
        }
    }

    fun playAgainQuiz(navHostController: NavHostController) {
        _uiState.value = UIState.Idle
        viewModelScope.launch {
            navHostController.popBackStack()
            navHostController.navigate(Destination.StartQuiz.passArgument(category, level))
        }
    }
}