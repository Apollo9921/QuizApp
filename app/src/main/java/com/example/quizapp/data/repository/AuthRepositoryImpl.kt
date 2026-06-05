package com.example.quizapp.data.repository

import com.example.quizapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {
    override suspend fun registerWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}