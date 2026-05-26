package com.example.quizapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    data class UserData(
        var totalPoints: Int = 0,
        var badge: Int = 0,
        var percentage: Double = 0.0
    )

    sealed class UIState {
        data object Idle : UIState()
        data class Success(val user: UserEntity, val userData: UserData) : UIState()
    }

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val result = repository.fetchUser()
            val data = UserData()
            data.totalPoints = result.totalPoints
            for (i in badgesPoints.indices) {
                if (result.totalPoints <= badgesPoints[i]) {
                    data.badge = badgesPoints[i]
                    break
                }
            }
            data.percentage = (data.totalPoints * 100) / data.badge.toDouble()
            data.percentage *= 0.01
            _uiState.value = UIState.Success(result, data)
        }
    }

}