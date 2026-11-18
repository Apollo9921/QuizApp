package com.apollo9921.quizrise.presentation.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateStreakUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class CategoriesViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase
) : ViewModel() {

    private val _streakUIState = MutableStateFlow<StreakUIState>(StreakUIState.Idle)
    val streakUIState: StateFlow<StreakUIState> = _streakUIState.asStateFlow()

    sealed class StreakUIState {
        data object Idle : StreakUIState()
        data class Success(val streak: Int, val lives: Int) : StreakUIState()
        data class Error(val message: Int) : StreakUIState()
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val result = fetchUserUseCase.invoke()
                result.onSuccess { user ->
                    val lastLoginDateMs = user.lastLoginDate
                    val lastLoginDate = Instant.ofEpochMilli(lastLoginDateMs)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    val today = LocalDate.now()
                    val daysBetween = ChronoUnit.DAYS.between(lastLoginDate, today)

                    when {
                        daysBetween == 1L -> {
                            val newStreak = user.currentStreak + 1
                            var newLives = user.currentLives

                            if (newStreak % 7 == 0) {
                                newLives += 3
                            } else if (newStreak % 3 == 0) {
                                newLives += 1
                            }

                            updateStreakUseCase.invoke(
                                currentStreak = newStreak,
                                currentLives = newLives,
                                lastLoginDate = today.toEpochDay(),
                                name = user.name
                            )

                            _streakUIState.value = StreakUIState.Success(newStreak, newLives)
                        }

                        daysBetween > 1L -> {
                            updateStreakUseCase.invoke(
                                currentStreak = 1,
                                currentLives = 0,
                                lastLoginDate = today.toEpochDay(),
                                name = user.name
                            )

                            _streakUIState.value = StreakUIState.Success(1, 0)
                        }

                        else -> {
                            _streakUIState.value =
                                StreakUIState.Success(user.currentStreak, user.currentLives)
                        }
                    }
                }
            } catch (_: Exception) {
                _streakUIState.value = StreakUIState.Error(R.string.unexpected_error)
            }
        }
    }
}