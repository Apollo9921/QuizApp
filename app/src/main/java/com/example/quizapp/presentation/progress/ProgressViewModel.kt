package com.example.quizapp.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.R
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.data.repository.UserRepositoryImpl
import com.example.quizapp.view.custom.badgesPoints
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val repository: UserRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _userUIState = MutableStateFlow(UserData())

    data class UserData(
        var totalPoints: Int = 0,
        var badge: Int = 0,
        var percentage: Double = 0.0
    )

    sealed class UIState {
        data object Idle : UIState()
        data class Success(val user: UserEntity, val userData: UserData) : UIState()
        data class Error(val errorMessage: Int) : UIState()
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val result = repository.fetchUser()
            if (result.isSuccess) {
                val data = result.getOrThrow()
                _userUIState.value.totalPoints = data.totalPoints
                for (i in badgesPoints.indices) {
                    if (data.totalPoints <= badgesPoints[i]) {
                        _userUIState.value.badge = badgesPoints[i]
                        break
                    }
                }
                _userUIState.value.percentage = (data.totalPoints * 100) / _userUIState.value.badge.toDouble()
                _userUIState.value.percentage *= 0.01
                _uiState.value = UIState.Success(data, _userUIState.value)
            } else {
                _uiState.value = UIState.Error(errorMessage = R.string.something_went_wrong)
            }
        }
    }

}