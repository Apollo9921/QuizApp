package com.apollo9921.quizrise.data.repository

import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {
    override suspend fun registerWithEmail(
        email: String,
        password: String
    ): AppResult<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error(AppError.Unknown)
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> AppResult.Error(AppError.Network)

                is FirebaseAuthInvalidCredentialsException ->
                    AppResult.Error(AppError.InvalidCredentials)

                is com.google.firebase.auth.FirebaseAuthUserCollisionException ->
                    AppResult.Error(AppError.UserAlreadyExists)

                else -> AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun signInWithGoogle(idToken: String): AppResult<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error(AppError.Unknown)
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> AppResult.Error(AppError.Network)
                else -> AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): AppResult<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error(AppError.Unknown)
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> AppResult.Error(AppError.Network)
                is FirebaseAuthInvalidUserException -> AppResult.Error(AppError.UserNotFound)
                is FirebaseAuthInvalidCredentialsException -> AppResult.Error(AppError.InvalidCredentials)
                else -> AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun checkIfUserExists(): AppResult<Boolean> {
        return withContext(ioDispatcher) {
            try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext AppResult.Error(AppError.Unknown)

                val document = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

                AppResult.Success(document.exists())
            } catch (_: Exception) {
                AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun signInAnonymously(): AppResult<Unit> {
        return try {
            val result = auth.signInAnonymously().await()
            if (result.user != null) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error(AppError.Unknown)
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> AppResult.Error(AppError.Network)
                else -> AppResult.Error(AppError.Unknown)
            }
        }
    }
}