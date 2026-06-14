package com.example.quizapp.presentation.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.R
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.usecase.FetchResultsUseCase
import com.example.quizapp.domain.usecase.FetchUserUseCase
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

    init {
        fetchUserAndResults()
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