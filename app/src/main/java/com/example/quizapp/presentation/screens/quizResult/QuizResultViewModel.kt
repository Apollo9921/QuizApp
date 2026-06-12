package com.example.quizapp.presentation.screens.quizResult

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.UpdateBadgeUseCase
import com.example.quizapp.domain.usecase.UpdatePointsUseCase
import com.example.quizapp.domain.usecase.UpdateResultsUseCase
import com.example.quizapp.domain.usecase.UpdateUserToRemoteUseCase
import com.example.quizapp.domain.util.PlayerLevel
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val updateResultsUseCase: UpdateResultsUseCase,
    private val updatePointsUseCase: UpdatePointsUseCase,
    private val updateUserToRemoteUseCase: UpdateUserToRemoteUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateBadgeUseCase: UpdateBadgeUseCase,
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) : ViewModel() {

    private var userName: String? = null
    private val pointsPossible: Int = 25

    var total: Int = 5
    var pointsReceived = mutableIntStateOf(0)

    init {
        saveQuizProcess()
    }

    private fun saveQuizProcess() {
        viewModelScope.launch {
            try {
                pointsReceived.intValue = correctAnswers * 5

                val userResult = fetchUserUseCase.invoke()
                val userLocal = userResult.getOrThrow()
                userName = userLocal.name

                updateResultsUseCase.invoke(category, correctAnswers, incorrectAnswers)
                updatePointsUseCase.invoke(userLocal.name, pointsReceived.intValue, pointsPossible)


                val badge = PlayerLevel.getLevelByPoints(userResult.getOrThrow().totalPoints).badgeName
                updateBadgeUseCase.invoke(badge, userName ?: "")

                val userRemote = User(userLocal.name, pointsReceived.intValue, pointsPossible, badge)
                val resultsRemote = Results(category, correctAnswers, incorrectAnswers)

                updateUserToRemoteUseCase.invoke(userRemote, resultsRemote)

            } catch (_: Exception) {

            }
        }
    }
}