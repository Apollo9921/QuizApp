package com.example.quizapp.presentation.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.usecase.FetchBadgeImageUseCase
import com.example.quizapp.domain.usecase.FetchBadgeLevelUseCase
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.FetchBadgeUseCase
import com.example.quizapp.domain.usecase.UpdateBadgeUseCase
import com.example.quizapp.presentation.dataStore.UserManager
import com.example.quizapp.presentation.dataStore.dataStoreUser
import com.example.quizapp.presentation.utils.badgesDescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.toString

class ProfileViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchBadgeLevelUseCase: FetchBadgeLevelUseCase,
    private val fetchBadgeImageUseCase: FetchBadgeImageUseCase,
    private val fetchBadgeUseCase: FetchBadgeUseCase,
    private val updateBadgeUseCase: UpdateBadgeUseCase
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

    fun fetchUser(context: Context) {
        viewModelScope.launch {
            val userManager = UserManager(dataStore = context.dataStoreUser)
            val userName = userManager.userName.first().toString()
            val result = fetchUserUseCase.invoke()
            if (result.isSuccess) {
                val data = result.getOrThrow()
                val level = fetchBadgeLevelUseCase.invoke(data)
                val badge = fetchBadgeImageUseCase.invoke(data, context)
                val index = fetchBadgeUseCase.invoke(data, badge)
                if (index > -1) {
                    updateBadge(
                        context.getString(badgesDescription[index + 1]),
                        userName
                    )
                }

                _badgeState.value = Badge(badge, level)
                _uiState.value = UIState.Success(user = data)
            }
        }
    }

    private fun updateBadge(badge: String, name: String) {
        viewModelScope.launch {
            updateBadgeUseCase.invoke(badge, name)
        }
    }

}