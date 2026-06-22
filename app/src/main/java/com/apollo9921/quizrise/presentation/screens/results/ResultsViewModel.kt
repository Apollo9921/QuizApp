package com.apollo9921.quizrise.presentation.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.FetchResultsUseCase
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchResultsUseCase: FetchResultsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: MutableStateFlow<UIState> = _uiState

    sealed class UIState {
        object Loading : UIState()
        data class Success(val results: List<Results>, val user: User) : UIState()
        data class Error(val message: Int) : UIState()
    }

    fun fetchUserAndResults() {
        viewModelScope.launch {
            try {
                val userResult = fetchUserUseCase.invoke()
                val resultsResult = fetchResultsUseCase.invoke()
                if (userResult.isSuccess && resultsResult.isSuccess) {
                    val user = userResult.getOrNull()
                    val results = resultsResult.getOrNull()
                    if (user != null && results != null) {
                        _uiState.value = UIState.Success(results, user)
                    } else {
                        _uiState.value = UIState.Error(R.string.unexpected_error)
                    }
                } else {
                    _uiState.value = UIState.Error(R.string.unexpected_error)
                }
            } catch (_: Exception) {
                _uiState.value = UIState.Error(R.string.unexpected_error)
            }
        }
    }
}