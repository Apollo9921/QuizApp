package com.example.quizapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.repository.GoogleAuthService
import com.example.quizapp.presentation.navigation.Destination
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

    fun startSignInByGoogle(navHostController: NavHostController) {
        viewModelScope.launch {
            val result = googleAuthService.getGoogleIdToken()
            result.fold(
                onSuccess = { idToken ->
                    signInWithGoogle(idToken, navHostController)
                },
                onFailure = {

                }
            )
        }
    }

    private fun signInWithGoogle(idToken: String, navHostController: NavHostController) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            result.fold(
                onSuccess = {
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.CreateUser.route)
                },
                onFailure = {

                }
            )
        }
    }

}