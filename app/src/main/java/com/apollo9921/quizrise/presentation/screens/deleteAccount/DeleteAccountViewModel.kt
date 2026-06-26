package com.apollo9921.quizrise.presentation.screens.deleteAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.DeleteAccountUseCase
import com.apollo9921.quizrise.presentation.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val deleteAccountUseCase: DeleteAccountUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    sealed class UIState {
        data object Idle: UIState()
        data object Loading: UIState()
        data class Error(val message: Int): UIState()
    }

    fun deleteAccount(navHostController: NavHostController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val response = deleteAccountUseCase.invoke()
            if (response is AppResult.Success) {
                _uiState.value = UIState.Idle
                navHostController.navigate(Destination.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            } else if (response is AppResult.Error) {
                when(response.error) {
                    AppError.Network -> {
                        _uiState.value = UIState.Error(R.string.network_error)
                    }
                    AppError.NoInternetConnection -> {
                        _uiState.value = UIState.Error(R.string.no_internet_connection)
                    }
                    AppError.Timeout -> {
                        _uiState.value = UIState.Error(R.string.request_timeout)
                    }
                    AppError.Unauthorized -> {
                        _uiState.value = UIState.Error(R.string.invalid_request)
                    }
                    AppError.Unknown -> {
                        _uiState.value = UIState.Error(R.string.unexpected_error)
                    }
                    else -> {
                        _uiState.value = UIState.Error(R.string.unexpected_error)
                    }
                }
            }
        }
    }
}