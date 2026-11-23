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
                    val updateStreakUseCaseResult = updateStreakUseCase.invoke(user)
                    _streakUIState.value = StreakUIState.Success(
                        streak = updateStreakUseCaseResult.first,
                        lives = updateStreakUseCaseResult.second
                    )
                }
            } catch (_: Exception) {
                _streakUIState.value = StreakUIState.Error(R.string.unexpected_error)
            }
        }
    }
}