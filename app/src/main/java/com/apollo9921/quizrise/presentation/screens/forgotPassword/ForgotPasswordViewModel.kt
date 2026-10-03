package com.apollo9921.quizrise.presentation.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.UpdatePasswordByLinkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val updatePasswordByLinkUseCase: UpdatePasswordByLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    sealed class UIState {
        data object Idle : UIState()
        data object Loading : UIState()
        data class Success(val message: Int) : UIState()
        data class Error(val message: Int) : UIState()
    }

    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun sendResetLink(email: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            when (val result = updatePasswordByLinkUseCase.invoke(email)) {
                is AppResult.Error -> {
                    when (result.error) {
                        is AppError.EmptyFields -> {
                            _uiState.value = UIState.Error(message = R.string.empty_fields)
                        }

                        is AppError.InvalidEmailFormat -> {
                            _uiState.value = UIState.Error(message = R.string.invalid_email_format)
                        }

                        is AppError.Network -> {
                            _uiState.value = UIState.Error(message = R.string.no_internet_connection)
                        }

                        else -> {
                            _uiState.value = UIState.Error(message = R.string.unexpected_error)
                        }
                    }
                }

                is AppResult.Success<*> -> {
                    _uiState.value = UIState.Success(message = R.string.link_sent)
                }
            }
        }
    }
}