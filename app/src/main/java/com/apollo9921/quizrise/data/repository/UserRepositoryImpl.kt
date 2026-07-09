package com.apollo9921.quizrise.data.repository

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.apollo9921.quizrise.data.local.dao.UserDAO
import com.apollo9921.quizrise.data.mapper.toUser
import com.apollo9921.quizrise.data.mapper.toUserEntity
import com.apollo9921.quizrise.data.worker.UpdateUserWorker
import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class UserRepositoryImpl(
    private val userDAO: UserDAO,
    private val firestore: FirebaseFirestore,
    private val workManager: WorkManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    private fun scheduleUpdateWorker(user: User, results: Results) {
        val data = workDataOf(
            "totalPoints" to user.totalPoints,
            "totalPointsPossible" to user.totalPointsPossible,
            "badge" to user.badge,
            "category" to results.category,
            "correct" to results.correctAnswers,
            "incorrect" to results.incorrectAnswers
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val updateRequest = OneTimeWorkRequestBuilder<UpdateUserWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()

        workManager.enqueue(updateRequest)
    }

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

    override suspend fun postUserAndResults(user: User, results: List<Results>): AppResult<Unit> {
        return withContext(ioDispatcher) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser == null) {
                return@withContext AppResult.Error(AppError.Unauthorized)
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

            } catch (_: HttpRequestTimeoutException) {
                AppResult.Error(AppError.Timeout)
            } catch (_: ConnectTimeoutException) {
                AppResult.Error(AppError.NoInternetConnection)
            } catch (_: IOException) {
                AppResult.Error(AppError.Network)
            } catch (_: RedirectResponseException) {
                AppResult.Error(AppError.Server)
            } catch (_: ClientRequestException) {
                AppResult.Error(AppError.BadRequest)
            } catch (_: ServerResponseException) {
                AppResult.Error(AppError.ServerDown)
            } catch (_: Exception) {
                AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun updateUserAndResults(
        user: User,
        results: Results
    ): AppResult<Unit> {
        return withContext(ioDispatcher) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser == null) {
                return@withContext AppResult.Error(AppError.Unauthorized)
            }

            val userId = currentUser.uid

            try {
                val userRef = firestore.collection("users").document(userId)
                val resultRef = userRef.collection("results").document(results.category)

                val batch = firestore.batch()

                batch.update(
                    userRef,
                    "badge", user.badge,
                    "totalPoints", FieldValue.increment(user.totalPoints.toLong()),
                    "totalPointsPossible", FieldValue.increment(user.totalPointsPossible.toLong())
                )

                batch.set(
                    resultRef,
                    mapOf(
                        "correctAnswers" to FieldValue.increment(results.correctAnswers.toLong()),
                        "incorrectAnswers" to FieldValue.increment(results.incorrectAnswers.toLong()),
                        "category" to results.category
                    ),
                    com.google.firebase.firestore.SetOptions.merge()
                )

                batch.commit().await()
                AppResult.Success(Unit)

            } catch (e: Exception) {
                if (e is UnknownHostException || e.cause is UnknownHostException) {
                    scheduleUpdateWorker(user, results)
                    AppResult.Success(Unit)
                } else {
                    AppResult.Error(AppError.Unknown)
                }
            }
        }
    }

    override suspend fun getUser(): AppResult<User> {
        return withContext(ioDispatcher) {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                    ?: return@withContext AppResult.Error(AppError.Unauthorized)

                val document = firestore.collection("users").document(userId).get().await()
                val user = document.toObject(User::class.java)

                if (user != null) {
                    AppResult.Success(user)
                } else {
                    AppResult.Error(AppError.Unknown)
                }
            } catch (_: HttpRequestTimeoutException) {
                AppResult.Error(AppError.Timeout)
            } catch (_: ConnectTimeoutException) {
                AppResult.Error(AppError.NoInternetConnection)
            } catch (_: IOException) {
                AppResult.Error(AppError.Network)
            } catch (_: RedirectResponseException) {
                AppResult.Error(AppError.Server)
            } catch (_: ClientRequestException) {
                AppResult.Error(AppError.BadRequest)
            } catch (_: ServerResponseException) {
                AppResult.Error(AppError.ServerDown)
            } catch (_: Exception) {
                AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun getResults(): AppResult<List<Results>> {
        return withContext(ioDispatcher) {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                    ?: return@withContext AppResult.Error(AppError.Unauthorized)

                val snapshot = firestore.collection("users")
                    .document(userId)
                    .collection("results")
                    .get()
                    .await()

                val resultsList = snapshot.toObjects(Results::class.java)
                AppResult.Success(resultsList)
            } catch (_: HttpRequestTimeoutException) {
                AppResult.Error(AppError.Timeout)
            } catch (_: ConnectTimeoutException) {
                AppResult.Error(AppError.NoInternetConnection)
            } catch (_: IOException) {
                AppResult.Error(AppError.Network)
            } catch (_: RedirectResponseException) {
                AppResult.Error(AppError.Server)
            } catch (_: ClientRequestException) {
                AppResult.Error(AppError.BadRequest)
            } catch (_: ServerResponseException) {
                AppResult.Error(AppError.ServerDown)
            } catch (_: Exception) {
                AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun clearAllData() {
        userDAO.clearAllData()
    }

    override suspend fun deleteAccount(): AppResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                    ?: return@withContext AppResult.Error(AppError.Unauthorized)

                val userId = currentUser.uid
                val userRef = firestore.collection("users").document(userId)
                val resultsRef = userRef.collection("results")

                val resultsSnapshot = resultsRef.get().await()

                val batch = firestore.batch()

                for (document in resultsSnapshot.documents) {
                    batch.delete(document.reference)
                }

                batch.delete(userRef)
                batch.commit().await()
                currentUser.delete().await()
                AppResult.Success(Unit)

            } catch (e: Exception) {
                when (e) {
                    is HttpRequestTimeoutException -> AppResult.Error(AppError.Timeout)
                    is ConnectTimeoutException -> AppResult.Error(AppError.NoInternetConnection)
                    is IOException -> AppResult.Error(AppError.Network)
                    is com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException -> {
                        AppResult.Error(AppError.Unauthorized)
                    }

                    else -> AppResult.Error(AppError.Unknown)
                }
            }
        }
    }

    override suspend fun postSession(session: String, user: User): AppResult<Unit> {
        return withContext(ioDispatcher) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser == null) {
                return@withContext AppResult.Error(AppError.Unauthorized)
            }

            val userId = currentUser.uid

            try {
                val userRef = firestore.collection("users").document(userId)

                val batch = firestore.batch()

                batch.update(
                    userRef,
                    "session", session,
                )

                batch.commit().await()
                AppResult.Success(Unit)

            } catch (_: Exception) {
                AppResult.Error(AppError.Unknown)
            }
        }
    }

    override suspend fun updateSession(
        session: String,
        user: User
    ): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val result = userDAO.updateSession(session, user.name)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}