package com.example.quizapp.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.quizapp.domain.repository.GoogleAuthService
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.quizapp.BuildConfig

class GoogleAuthServiceImpl(
    private val context: Context
) : GoogleAuthService {

    override suspend fun getGoogleIdToken(): Result<String> {
        return try {
            val credentialManager = CredentialManager.Companion.create(context)
            val googleSignIn = configureGoogleSignIn()

            val result = credentialManager.getCredential(
                request = googleSignIn,
                context = context
            )

            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential =
                    GoogleIdTokenCredential.Companion.createFrom(credential.data)

                Result.success(googleCredential.idToken)
            } else {
                Result.failure(Exception("Invalid credential type"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun configureGoogleSignIn(): GetCredentialRequest {
        val googleSignInOptions = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(true)
            .build()

        val client = GetCredentialRequest.Builder().addCredentialOption(googleSignInOptions).build()
        return client
    }
}