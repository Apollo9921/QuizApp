package com.example.quizapp.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.usecase.FetchBadgeImageUseCase
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.FetchBadgeUseCase
import com.example.quizapp.domain.util.PlayerLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchBadgeImageUseCase: FetchBadgeImageUseCase,
    private val fetchBadgeUseCase: FetchBadgeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _badgeState = MutableStateFlow<Badge>(Badge())
    val badgeState = _badgeState.asStateFlow()

    data class Badge(
        var badge: Int = 0,
        var badgeLevel: Int = 0
    )

    sealed class UIState {
        data object Idle : UIState()
        data class Success(val user: User) : UIState()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val result = fetchUserUseCase.invoke()
            if (result.isSuccess) {
                var data = result.getOrThrow()
                val badgeSymbol = fetchBadgeImageUseCase.invoke(data)
                val badgeMaxPoints = fetchBadgeUseCase.invoke(data)

                val badgeName = PlayerLevel.getLevelByPoints(data.totalPoints).badgeName
                data = data.copy(badge = badgeName)

                _badgeState.value = Badge(badgeSymbol, badgeMaxPoints)
                _uiState.value = UIState.Success(user = data)
            }
        }
    }

}