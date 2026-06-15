package com.example.quizapp.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.R
import com.example.quizapp.domain.result.AppError
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
                is AppResult.Error -> {
                    when(result.error) {
                        is AppError.EmptyFields -> {
                            _uiState.value = UIState.Error(message = R.string.empty_fields)
                        }
                        is AppError.InvalidEmailFormat -> {
                            _uiState.value = UIState.Error(message = R.string.invalid_email_format)
                        }
                        is AppError.PasswordLength -> {
                            _uiState.value = UIState.Error(message = R.string.password_length)
                        }
                        is AppError.PasswordMismatch -> {
                            _uiState.value = UIState.Error(message = R.string.password_mismatch)
                        }
                        else -> {
                            _uiState.value = UIState.Error(message = R.string.unexpected_error)
                        }
                    }
                }
                is AppResult.Success<*> -> {
                    _uiState.value = UIState.Idle
                    onNavigateBack()
                }
            }
        }
    }
}