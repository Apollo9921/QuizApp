package com.apollo9921.quizrise.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.ClearAllDataUseCase
import com.apollo9921.quizrise.domain.usecase.FetchBadgeImageUseCase
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.FetchBadgeUseCase
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchBadgeImageUseCase: FetchBadgeImageUseCase,
    private val fetchBadgeUseCase: FetchBadgeUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val clearAllDataUseCase: ClearAllDataUseCase
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

    fun logout(navHostController: NavHostController) {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                clearAllDataUseCase.invoke()
                _uiState.value = UIState.Idle
                navHostController.navigate(Destination.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}