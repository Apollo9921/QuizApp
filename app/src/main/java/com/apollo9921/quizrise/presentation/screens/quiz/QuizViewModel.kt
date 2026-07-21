package com.apollo9921.quizrise.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.data.network.dto.TranslatedQuizResult
import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.repository.GoogleAuthService
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.FormatQuizUseCase
import com.apollo9921.quizrise.domain.usecase.GetQuizUseCase
import com.apollo9921.quizrise.domain.usecase.PostSessionUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateUserSessionUseCase
import com.apollo9921.quizrise.presentation.navigation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    private val formatQuizUseCase: FormatQuizUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val postSessionUseCase: PostSessionUseCase,
    private val updateUserSessionUseCase: UpdateUserSessionUseCase,
    private val googleAuthService: GoogleAuthService,
    private val authRepository: AuthRepository,
    val category: String,
    val level: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _quizState = MutableStateFlow<QuizState>(QuizState())
    val quizState: StateFlow<QuizState> = _quizState

    private var timerJob: Job? = null
    private var hasNavigatedToResult = false

    data class QuizState(
        var progress: Int = 20,
        var correctAnswers: Int = 0,
        var incorrectAnswers: Int = 0,
        val session: String = ""
    )

    init {
        getQuiz()
    }

    sealed class UIState {
        data object Loading : UIState()
        data class Success(val quiz: List<TranslatedQuizResult>) : UIState()
        data class Error(val errorMessage: Int, val showToast: Boolean = false) : UIState()
    }

    fun getQuiz() {
        viewModelScope.launch {
            try {
                _uiState.value = UIState.Loading
                val userResult = fetchUserUseCase.invoke()
                if (userResult.isSuccess) {
                    _quizState.value =
                        _quizState.value.copy(session = userResult.getOrThrow().session)
                }
                val result = getQuizUseCase.invoke(category, level, _quizState.value.session)

                when (result) {
                    is AppResult.Error -> {
                        when (result.error) {
                            is AppError.NoCategoryOrLevelDefined -> {
                                _uiState.value =
                                    UIState.Error(R.string.no_category_or_level_defined)
                            }

                            is AppError.NoInternetConnection -> {
                                _uiState.value = UIState.Error(R.string.no_internet_connection)
                            }

                            is AppError.Network -> {
                                _uiState.value = UIState.Error(R.string.network_error)
                            }

                            is AppError.Server -> {
                                _uiState.value = UIState.Error(R.string.server_error)
                            }

                            is AppError.ServerDown -> {
                                _uiState.value = UIState.Error(R.string.server_down)
                            }

                            is AppError.Unknown -> {
                                _uiState.value = UIState.Error(R.string.unexpected_error)
                            }

                            is AppError.AnonymousUserExpiredQuiz -> {
                                _uiState.value = UIState.Error(R.string.anonymous_quiz_expired)
                            }

                            else -> {
                                _uiState.value = UIState.Error(R.string.unexpected_error)
                            }
                        }
                    }

                    is AppResult.Success -> {
                        if (result.data.isEmpty()) {
                            val newSession = postSessionUseCase.invoke()
                            if (newSession is AppResult.Success) {
                                val result = updateUserSessionUseCase.invoke(newSession.data.id, userResult.getOrThrow())
                                if (result is AppResult.Error) {
                                    _uiState.value = UIState.Error(R.string.unexpected_error)
                                    return@launch
                                } else {
                                    _quizState.value = _quizState.value.copy(session = newSession.data.id)
                                    getQuiz()
                                    return@launch
                                }
                            } else {
                                _uiState.value = UIState.Error(R.string.unexpected_error)
                            }
                        } else {
                            val translatedQuiz = formatQuizUseCase.invoke(result.data)
                            if (translatedQuiz is AppResult.Success) {
                                _uiState.value = UIState.Success(translatedQuiz.data)
                                timing()
                            } else {
                                _uiState.value = UIState.Error(R.string.unexpected_error)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState.Error(R.string.unexpected_error)
            }
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
        if (currentPage >= previousState.quiz.size - 1 && !hasNavigatedToResult) {
            hasNavigatedToResult = true
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
        hasNavigatedToResult = false
        _quizState.value = QuizState()
    }

    fun startSignInByGoogle(navHostController: NavHostController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = googleAuthService.getGoogleIdToken()
            when (result) {
                is AppResult.Error -> {
                    when(result.error) {
                        is AppError.NoInternetConnection -> {
                            _uiState.value = UIState.Error(R.string.no_internet_connection, showToast = true)
                        }
                        is AppError.Network -> {
                            _uiState.value = UIState.Error(R.string.network_error, showToast = true)
                        }
                        is AppError.Server -> {
                            _uiState.value = UIState.Error(R.string.server_error, showToast = true)
                        }
                        is AppError.ServerDown -> {
                            _uiState.value = UIState.Error(R.string.server_down, showToast = true)
                        }
                        else -> {
                            _uiState.value = UIState.Error(R.string.unexpected_error, showToast = true)
                        }
                    }
                }

                is AppResult.Success<*> -> {
                    signInWithGoogle(result.data.toString(), navHostController)
                }
            }
        }
    }

    private fun signInWithGoogle(idToken: String, navHostController: NavHostController) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogleByAnonymouslyAccount(idToken)
            when (result) {
                is AppResult.Error -> {
                    when(result.error) {
                        is AppError.NoInternetConnection -> {
                            _uiState.value = UIState.Error(R.string.no_internet_connection, showToast = true)
                        }
                        is AppError.Network -> {
                            _uiState.value = UIState.Error(R.string.network_error, showToast = true)
                        }
                        is AppError.Server -> {
                            _uiState.value = UIState.Error(R.string.server_error, showToast = true)
                        }
                        is AppError.ServerDown -> {
                            _uiState.value = UIState.Error(R.string.server_down, showToast = true)
                        }
                        is AppError.UserAlreadyExists -> {
                            _uiState.value = UIState.Error(R.string.user_already_exists, showToast = true)
                        }
                        else -> {
                            _uiState.value = UIState.Error(R.string.unexpected_error, showToast = true)
                        }
                    }
                }

                is AppResult.Success<*> -> {
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.Categories.route)
                }
            }
        }
    }
}