package com.apollo9921.quizrise.presentation.screens.settings

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.domain.usecase.ClearAllDataUseCase
import com.apollo9921.quizrise.domain.usecase.DeleteAccountUseCase
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {
    private val _isGdprConsentGranted = MutableStateFlow(false)
    val isGdprConsentGranted: StateFlow<Boolean> = _isGdprConsentGranted


    fun logout(navHostController: NavHostController) {
        viewModelScope.launch {
            try {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    if (user.isAnonymous) {
                        deleteAccountUseCase.invoke()
                    }
                }
                firebaseAuth.signOut()
                clearAllDataUseCase.invoke()
                navHostController.navigate(Destination.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteAccount(navHostController: NavHostController) {
        viewModelScope.launch {
            navHostController.navigate(Destination.DeleteAccount.route)
        }
    }

    fun showPrivacyPolicy(context: Context) {
        viewModelScope.launch {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            customTabsIntent.launchUrl(
                context,
                Uri.parse("https://apollo9921.github.io/quizrise-privacy-policy/")
            )
        }
    }

    fun changeGdprConsent(value: Boolean): Boolean {
        _isGdprConsentGranted.value = value
        return value
    }
}