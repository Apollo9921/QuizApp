package com.apollo9921.quizrise.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.PostUserUseCase
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
                        is AppError.UserAlreadyExists -> {
                            _uiState.value = UIState.Error(message = R.string.user_already_exists)
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