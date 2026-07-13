package com.apollo9921.quizrise.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.apollo9921.quizrise.BuildConfig
import com.apollo9921.quizrise.domain.repository.GoogleAuthService
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.suspendCancellableCoroutine

class GoogleAuthServiceImpl(
    private val context: Context,
    private val launcherHolder: GoogleSignInLauncherHolder
) : GoogleAuthService {

    override suspend fun getGoogleIdToken(): AppResult<String> {
        val credentialManager = CredentialManager.create(context)

        return try {
            executeGetCredential(credentialManager, filterAuthorized = true)
        } catch (e: GetCredentialException) {
            try {
                executeGetCredential(credentialManager, filterAuthorized = false)
            } catch (e2: GetCredentialException) {
                if (e2 is GetCredentialCancellationException) {
                    AppResult.Error(AppError.Unknown)
                } else {
                    legacyGoogleSignIn()
                }
            }
        }
    }

    private suspend fun executeGetCredential(
        credentialManager: CredentialManager,
        filterAuthorized: Boolean
    ): AppResult<String> {
        val request = configureGoogleSignIn(filterAuthorized)
        val result = credentialManager.getCredential(
            request = request,
            context = context
        )

        val credential = result.credential
        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            AppResult.Success(googleCredential.idToken)
        } else {
            AppResult.Error(AppError.InvalidCredentials)
        }
    }

    private fun configureGoogleSignIn(filterByAuthorizedAccounts: Boolean): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setAutoSelectEnabled(false)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    private suspend fun legacyGoogleSignIn(): AppResult<String> {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(context, gso)

        return suspendCancellableCoroutine { continuation ->
            launcherHolder.launch(client.signInIntent, continuation)
        }
    }
}