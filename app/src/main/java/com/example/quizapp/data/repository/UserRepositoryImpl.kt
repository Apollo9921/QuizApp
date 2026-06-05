package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.UserDAO
import com.example.quizapp.data.mapper.toUser
import com.example.quizapp.data.mapper.toUserEntity
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userDAO: UserDAO,
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun fetchUser(): Result<User> {
        return withContext(ioDispatcher) {
            try {
                val result = userDAO.fetchUser()
                Result.success(result.toUser())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun insertUser(user: User) {
        withContext(ioDispatcher) {
            userDAO.insertUser(user.toUserEntity())
        }
    }

    override suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String) {
        withContext(ioDispatcher) {
            userDAO.updatePoints(totalPoints, totalPointsPossible, name)
        }
    }

    override suspend fun updateBadge(badge: String, name: String) {
        withContext(ioDispatcher) {
            userDAO.updateBadge(badge, name)
        }
    }

    override suspend fun saveUserAndResults(user: User, results: List<Results>): AppResult<Unit> {
        return withContext(ioDispatcher) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser == null) {
                return@withContext AppResult.Error(Exception("Firestore impedido: Utilizador não está autenticado no Firebase Auth."))
            }

            val userId = currentUser.uid

            try {
                val userRef = firestore.collection("users").document(userId)
                userRef.set(user).await()

                val batch = firestore.batch()
                results.forEach { result ->
                    val resultRef = userRef.collection("results").document(result.category)
                    batch.set(resultRef, result)
                }

                batch.commit().await()
                AppResult.Success(Unit)

            } catch (e: Exception) {
                AppResult.Error(e)
            }
        }
    }
}