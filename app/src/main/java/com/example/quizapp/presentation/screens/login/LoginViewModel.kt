package com.example.quizapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.repository.GoogleAuthService
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val googleAuthService: GoogleAuthService
) : ViewModel() {

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            authRepository.loginWithEmail(email, password)
        }
    }

    fun startSignInByGoogle() {
        viewModelScope.launch {
            val result = googleAuthService.getGoogleIdToken()
            result.fold(
                onSuccess = { idToken ->
                    signInWithGoogle(idToken)
                },
                onFailure = {

                }
            )
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken)
        }
    }

}