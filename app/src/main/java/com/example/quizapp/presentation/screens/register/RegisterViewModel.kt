package com.example.quizapp.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.result.AppResult
import com.example.quizapp.domain.usecase.PostUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val postUserUseCase: PostUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    sealed class UIState {
        data object Idle: UIState()
        data object Loading: UIState()
        data class Error(val message: Int): UIState()
    }

    fun onRegisterClick(
        email: String,
        password: String,
        confirmPassword: String,
        onNavigateBack: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = postUserUseCase.invoke(email, password, confirmPassword)
            when(result) {
                is AppResult.Error<*> -> {
                    _uiState.value = UIState.Error(message = result.message as Int)
                }
                is AppResult.Success<*> -> {
                    _uiState.value = UIState.Idle
                    onNavigateBack()
                }
            }
        }
    }
}