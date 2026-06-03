package com.example.quizapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.domain.usecase.GetQuizUseCase
import com.example.quizapp.view.navigation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    val category: String,
    val level: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _quizState = MutableStateFlow<QuizState>(QuizState())
    val quizState: StateFlow<QuizState> = _quizState

    private var timerJob: Job? = null

    data class QuizState(
        var progress: Int = 20,
        var correctAnswers: Int = 0,
        var incorrectAnswers: Int = 0,
    )

    init {
        getQuiz()
    }

    sealed class UIState {
        data object Loading : UIState()
        data class Success(val quiz: List<QuizDTO>, val answers: ArrayList<String>) : UIState()
        data class Error(val errorMessage: Int) : UIState()
    }

    fun getQuiz() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val data = getQuizUseCase.invoke(
                category.replace(" ", "_").lowercase(),
                level.lowercase()
            )
            val answers: ArrayList<String> = ArrayList()
            for (i in 0 until data.size) {
                answers.add(data[i].correctAnswer)
                answers.addAll(data[i].incorrectAnswers)
            }
            _uiState.value = UIState.Success(data, answers)
            timing()
        }
    }

    fun timing() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _quizState.value = _quizState.value.copy(progress = 20)
            while (_quizState.value.progress >= 0) {
                delay(1000L)
                _quizState.value = _quizState.value.copy(
                    progress = _quizState.value.progress - 1
                )
            }
        }
    }

    fun incrementCorrectAnswer(currentPage: Int, navHostController: NavHostController) {
        viewModelScope.launch {
            _quizState.value = _quizState.value.copy(
                correctAnswers = _quizState.value.correctAnswers + 1
            )
            checkCurrentPage(currentPage, navHostController)
            timing()
        }
    }

    fun incrementIncorrectAnswer(currentPage: Int, navHostController: NavHostController) {
        viewModelScope.launch {
            _quizState.value = _quizState.value.copy(
                incorrectAnswers = _quizState.value.incorrectAnswers + 1
            )
            checkCurrentPage(currentPage, navHostController)
            timing()
        }
    }

    private fun checkCurrentPage(currentPage: Int, navHostController: NavHostController) {
        val previousState = _uiState.value as UIState.Success
        if (currentPage >= previousState.quiz.size - 1) {
            navHostController.navigate(
                Destination.QuizResult.passArgument(
                    category = category,
                    correctAnswers = _quizState.value.correctAnswers,
                    incorrectAnswers = _quizState.value.incorrectAnswers
                )
            )
            resetValues()
        }
    }

    fun resetValues() {
        timerJob?.cancel()
        _quizState.value = QuizState()
    }
}