package com.apollo9921.quizrise.presentation.screens.quizResult

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateBadgeUseCase
import com.apollo9921.quizrise.domain.usecase.UpdatePointsUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateResultsUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateUserAndResultsUseCase
import com.apollo9921.quizrise.domain.util.PlayerLevel
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val updateResultsUseCase: UpdateResultsUseCase,
    private val updatePointsUseCase: UpdatePointsUseCase,
    private val updateUserAndResultsUseCase: UpdateUserAndResultsUseCase,
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

                val userRemote = User("", userLocal.name, pointsReceived.intValue, pointsPossible, badge)
                val resultsRemote = Results("", category, correctAnswers, incorrectAnswers)

                updateUserAndResultsUseCase.invoke(userRemote, resultsRemote)

            } catch (_: Exception) {

            }
        }
    }
}