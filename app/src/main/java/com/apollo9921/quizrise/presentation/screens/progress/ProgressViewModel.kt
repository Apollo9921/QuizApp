package com.apollo9921.quizrise.presentation.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.FormatProgressPercentageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val formatProgressPercentageUseCase: FormatProgressPercentageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    data class UserData(
        var currentPoints: Int = 0,
        var maxPoints: Int = 0,
        var percentage: Double = 0.0
    )

    sealed class UIState {
        data object Idle : UIState()
        data class Success(val user: User, val userData: UserData) : UIState()
        data class Error(val errorMessage: Int) : UIState()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val result = fetchUserUseCase.invoke()
            if (result.isSuccess) {
                val data = result.getOrThrow()
                val userData = formatProgressPercentageUseCase.invoke(data)
                _uiState.value = UIState.Success(data, userData)
            } else {
                _uiState.value = UIState.Error(errorMessage = R.string.something_went_wrong)
            }
        }
    }

}