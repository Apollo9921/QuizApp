package com.example.quizapp.presentation.screens.quizResult

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.UpdatePointsUseCase
import com.example.quizapp.domain.usecase.UpdateResultsUseCase
import com.example.quizapp.domain.usecase.UpdateUserToRemoteUseCase
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val updateResultsUseCase: UpdateResultsUseCase,
    private val updatePointsUseCase: UpdatePointsUseCase,
    private val updateUserToRemoteUseCase: UpdateUserToRemoteUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) : ViewModel() {

    private var userName: String? = null
    private val pointsPossible: Int = 25

    var total: Int = 5
    var pointsReceived = mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            //TODO IMPROVE LOGIC
            val user = fetchUserUseCase.invoke()
            userName = user.getOrThrow().name
            pointsReceived.intValue = correctAnswers * 5
            updateResults()
            updatePoints()
            updateToFireStore()
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
                userName = userName ?: return@launch,
                pointsReceived = pointsReceived.intValue,
                pointsPossible = pointsPossible
            )
        }
    }

    private fun updateToFireStore() {
        viewModelScope.launch {
            val user = User(userName ?: return@launch, pointsReceived.intValue, pointsPossible, "")
            val results = Results(category, correctAnswers, incorrectAnswers)
            updateUserToRemoteUseCase.invoke(user, results)
        }
    }
}