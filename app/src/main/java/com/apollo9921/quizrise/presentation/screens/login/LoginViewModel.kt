package com.apollo9921.quizrise.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.repository.GoogleAuthService
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.InsertResultsUseCase
import com.apollo9921.quizrise.domain.usecase.InsertNewResultsUseCase
import com.apollo9921.quizrise.domain.usecase.InsertUserUseCase
import com.apollo9921.quizrise.domain.usecase.InsertNewUserUseCase
import com.apollo9921.quizrise.domain.usecase.PostSessionUseCase
import com.apollo9921.quizrise.domain.usecase.PostUserAndResultsUseCase
import com.apollo9921.quizrise.domain.usecase.PostUserAnonymouslyUseCase
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val googleAuthService: GoogleAuthService,
    private val userRepository: UserRepository,
    private val insertUserUseCase: InsertUserUseCase,
    private val insertResultsUseCase: InsertResultsUseCase,
    private val insertNewUserUseCase: InsertNewUserUseCase,
    private val insertNewResultsUseCase: InsertNewResultsUseCase,
    private val postUserAndResultsUseCase: PostUserAndResultsUseCase,
    private val postSessionUseCase: PostSessionUseCase,
    private val postUserAnonymouslyUseCase: PostUserAnonymouslyUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    companion object {
        private val ADJECTIVES = listOf(
            "Golden", "Wise", "Mighty", "Ancient", "Bravo",
            "Swift", "Noble", "Ethereal", "Valiant", "Mystic"
        )

        private val HEROES = listOf(
            "Apollo", "Artemis", "Zeus", "Athena", "Hermes",
            "Ares", "Hera", "Poseidon", "Demeter", "Hephaestus",
            "Socrates", "Plato", "Aristotle", "Leonidas", "Pericles",
            "Odysseus", "Achilles", "Hercules", "Perseus", "Atlas"
        )

        fun generateRandomName(): String {
            val adjective = ADJECTIVES.random()
            val hero = HEROES.random()
            return "$adjective $hero"
        }
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    sealed class UIState {
        data object Idle : UIState()
        data object Loading : UIState()
        data class Error(val message: Int) : UIState()
    }

    fun loginWithEmail(email: String, password: String, navHostController: NavHostController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            if (email.isEmpty() || password.isEmpty()) {
                _uiState.value = UIState.Error(message = R.string.empty_fields)
                return@launch
            }
            val loginResult = authRepository.loginWithEmail(email, password)
            when (loginResult) {
                is AppResult.Error -> {
                    getTypeOfError(loginResult.error)
                }

                is AppResult.Success<*> -> {
                    checkIfUserExists(navHostController)
                }
            }
        }
    }

    fun startSignInByGoogle(navHostController: NavHostController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = googleAuthService.getGoogleIdToken()
            when (result) {
                is AppResult.Error -> {
                    firebaseAuth.signOut()
                    getTypeOfError(result.error)
                }

                is AppResult.Success<*> -> {
                    signInWithGoogle(result.data.toString(), navHostController)
                }
            }
        }
    }

    private fun signInWithGoogle(idToken: String, navHostController: NavHostController) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            when (result) {
                is AppResult.Error -> {
                    firebaseAuth.signOut()
                    getTypeOfError(result.error)
                }

                is AppResult.Success<*> -> {
                    checkIfUserExists(navHostController)
                }
            }
        }
    }

    fun signInAnonymously(navHostController: NavHostController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val name = generateRandomName()
            val result = postUserAnonymouslyUseCase.invoke(name, "")
            when (result) {
                is AppResult.Error -> {
                    firebaseAuth.signOut()
                    getTypeOfError(result.error)
                }
                is AppResult.Success<*> -> {
                    _uiState.value = UIState.Idle
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.Categories.route)
                }
            }
        }
    }

    private fun checkIfUserExists(navHostController: NavHostController) {
        viewModelScope.launch {
            val result = authRepository.checkIfUserExists()
            when (result) {
                is AppResult.Error -> {
                    firebaseAuth.signOut()
                    getTypeOfError(result.error)
                }

                is AppResult.Success<*> -> {
                    val exists = result.data as Boolean
                    if (exists) {
                        var remoteUserResult = userRepository.getUser()
                        if (remoteUserResult is AppResult.Success) {
                            insertUserUseCase.invoke(remoteUserResult.data)
                            val remoteResults = userRepository.getResults()
                            if (remoteResults is AppResult.Success) {
                                remoteResults.data.forEach { result ->
                                    insertResultsUseCase.invoke(result)
                                }
                                _uiState.value = UIState.Idle
                                navHostController.popBackStack()
                                navHostController.navigate(Destination.Categories.route)
                            }
                        }

                    } else {
                        val sessionResult = postSessionUseCase.invoke()
                        if (sessionResult is AppResult.Success) {
                            val session = sessionResult.data.id
                            val randomName = generateRandomName()
                            insertNewUserUseCase.invoke(randomName, session)
                            insertNewResultsUseCase.invoke(randomName)
                            postUserAndResults(randomName, session, navHostController)
                        } else if (sessionResult is AppResult.Error) {
                            firebaseAuth.signOut()
                            getTypeOfError(sessionResult.error)
                        }
                    }
                }
            }
        }
    }

    private fun postUserAndResults(
        name: String,
        session: String,
        navHostController: NavHostController
    ) {
        viewModelScope.launch {
            val result = postUserAndResultsUseCase.invoke(name, session)
            when (result) {
                is AppResult.Error -> {
                    firebaseAuth.signOut()
                    getTypeOfError(result.error)
                }

                is AppResult.Success<*> -> {
                    _uiState.value = UIState.Idle
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.Categories.route)
                }
            }
        }
    }

    private fun getTypeOfError(error: AppError) {
        when (error) {
            is AppError.Timeout -> {
                _uiState.value = UIState.Error(message = R.string.request_timeout)
            }

            is AppError.NoInternetConnection -> {
                _uiState.value = UIState.Error(message = R.string.no_internet_connection)
            }

            is AppError.Network -> {
                _uiState.value = UIState.Error(message = R.string.network_error)
            }

            is AppError.Server -> {
                _uiState.value = UIState.Error(message = R.string.server_error)
            }

            is AppError.ServerDown -> {
                _uiState.value = UIState.Error(message = R.string.server_down)
            }

            is AppError.Unknown -> {
                _uiState.value = UIState.Error(message = R.string.unexpected_error)
            }

            is AppError.InvalidCredentials -> {
                _uiState.value = UIState.Error(message = R.string.login_failed)
            }

            is AppError.UserNotFound -> {
                _uiState.value = UIState.Error(message = R.string.user_not_found)
            }

            is AppError.EmptyFields -> {
                _uiState.value = UIState.Error(message = R.string.empty_fields)
            }

            else -> {
                _uiState.value = UIState.Error(message = R.string.unexpected_error)
            }
        }
    }
}