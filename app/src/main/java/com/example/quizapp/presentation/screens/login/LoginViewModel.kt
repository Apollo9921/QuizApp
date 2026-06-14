package com.example.quizapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.repository.GoogleAuthService
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult
import com.example.quizapp.domain.usecase.InsertResultLocally
import com.example.quizapp.domain.usecase.InsertResultsUseCase
import com.example.quizapp.domain.usecase.InsertUserLocally
import com.example.quizapp.domain.usecase.InsertUserUseCase
import com.example.quizapp.domain.usecase.SaveUserToRemoteUseCase
import com.example.quizapp.presentation.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val googleAuthService: GoogleAuthService,
    private val userRepository: UserRepository,
    private val insertUserLocally: InsertUserLocally,
    private val insertResultLocally: InsertResultLocally,
    private val insertUserUseCase: InsertUserUseCase,
    private val insertResultsUseCase: InsertResultsUseCase,
    private val saveUserToRemoteUseCase: SaveUserToRemoteUseCase
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
        data object Idle: UIState()
        data object Loading: UIState()
        data class Error(val message: Int): UIState()
    }

    fun loginWithEmail(email: String, password: String, navHostController: NavHostController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = authRepository.loginWithEmail(email, password)
            when(result) {
                is AppResult.Error<*> -> {
                    _uiState.value = UIState.Error(message = result.message as Int)
                }
                is AppResult.Success<*> -> {
                    checkIfUserExists(navHostController)
                }
            }
        }
    }

    fun startSignInByGoogle(navHostController: NavHostController) {
        viewModelScope.launch {
            val result = googleAuthService.getGoogleIdToken()
            when(result) {
                is AppResult.Error<*> -> {
                    _uiState.value = UIState.Error(message = result.message as Int)
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
            when(result) {
                is AppResult.Error<*> -> {
                    _uiState.value = UIState.Error(message = result.message as Int)
                }
                is AppResult.Success<*> -> {
                    checkIfUserExists(navHostController)
                }
            }
        }
    }

    private fun checkIfUserExists(navHostController: NavHostController) {
        viewModelScope.launch {
            val result = authRepository.checkIfUserExists()
            when(result) {
                is AppResult.Error<*> -> {
                    _uiState.value = UIState.Error(message = result.message as Int)
                }
                is AppResult.Success<*> -> {
                    val exists = result.data as Boolean
                    if (exists) {
                        val remoteUserResult = userRepository.fetchUserFromRemote()
                        if (remoteUserResult is AppResult.Success) {
                            insertUserLocally.invoke(remoteUserResult.data)
                        }

                        val remoteResults = userRepository.fetchResultsFromRemote()
                        if (remoteResults is AppResult.Success) {
                            remoteResults.data.forEach { result ->
                                insertResultLocally.invoke(result)
                            }
                        }

                        _uiState.value = UIState.Idle
                        navHostController.popBackStack()
                        navHostController.navigate(Destination.Categories.route)
                    } else {
                        val randomName = generateRandomName()
                        insertUserUseCase.invoke(randomName)
                        insertResultsUseCase.invoke(randomName)
                        saveUserAndResults(randomName, navHostController)
                    }
                }
            }
        }
    }

    private fun saveUserAndResults(
        name: String,
        navHostController: NavHostController
    ) {
        viewModelScope.launch {
            val result = saveUserToRemoteUseCase.invoke(name)
            when(result) {
                is AppResult.Error<*> -> {
                    _uiState.value = UIState.Error(message = result.message as Int)
                }
                is AppResult.Success<*> -> {
                    _uiState.value = UIState.Idle
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.Categories.route)
                }
            }
        }
    }
}