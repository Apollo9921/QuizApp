package com.apollo9921.quizrise.presentation.screens.editUsername

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.FetchResultsUseCase
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditUsernameViewModel(
    private val updateNameUseCase: UpdateNameUseCase,
    private val fetUserUseCase: FetchUserUseCase,
    private val fetchResultsUseCase: FetchResultsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val errorMessage: Int) : UiState()
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val userResult = fetUserUseCase.invoke()
            val results = fetchResultsUseCase.invoke()
            if (userResult.isSuccess && results.isSuccess) {
                val oldName = userResult.getOrThrow().name
                val response = updateNameUseCase.invoke(name, oldName, results.getOrThrow())
                when(response) {
                    is AppResult.Error -> {
                        _uiState.value = UiState.Error(getErrorMessage(response.error))
                    }
                    is AppResult.Success<*> -> {
                        _uiState.value = UiState.Success
                    }
                }
            } else {
                _uiState.value = UiState.Error(R.string.unexpected_error)
            }
        }
    }

    private fun getErrorMessage(error: AppError): Int {
        return when (error) {
            is AppError.EmptyFields -> R.string.empty_fields
            is AppError.SameName -> R.string.same_name_error
            is AppError.Unauthorized -> R.string.unexpected_error
            is AppError.Timeout -> R.string.request_timeout
            is AppError.NoInternetConnection -> R.string.no_internet_connection
            is AppError.Network -> R.string.network_error
            is AppError.Server -> R.string.server_error
            is AppError.BadRequest -> R.string.invalid_request
            is AppError.ServerDown -> R.string.server_down
            else -> R.string.unexpected_error
        }
    }

}