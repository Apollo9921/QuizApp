package com.example.quizapp.data.repository

import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.result.AppError
import com.example.quizapp.domain.result.AppResult
import com.google.firebase.auth.FirebaseAuth
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
        } catch (_: Exception) {
            AppResult.Error(AppError.Unknown)
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
        } catch (_: Exception) {
            AppResult.Error(AppError.Unknown)
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
        } catch (_: Exception) {
            AppResult.Error(AppError.Unknown)
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
}