package com.example.quizapp.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.example.quizapp.BuildConfig
import com.example.quizapp.domain.repository.GoogleAuthService
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthServiceImpl(
    private val context: Context
) : GoogleAuthService {

    override suspend fun getGoogleIdToken(): Result<String> {
        val credentialManager = CredentialManager.create(context)

        return try {
            executeGetCredential(credentialManager, filterAuthorized = true)
        } catch (_: NoCredentialException) {
            try {
                executeGetCredential(credentialManager, filterAuthorized = false)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun executeGetCredential(
        credentialManager: CredentialManager,
        filterAuthorized: Boolean
    ): Result<String> {
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
            Result.success(googleCredential.idToken)
        } else {
            Result.failure(Exception("Invalid credential type"))
        }
    }

    private fun configureGoogleSignIn(filterByAuthorizedAccounts: Boolean): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setAutoSelectEnabled(true)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
}