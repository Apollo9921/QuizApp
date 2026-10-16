package com.apollo9921.quizrise.data.repository

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.LeaderboardRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException

class LeaderboardRepositoryImpl(private val firestore: FirebaseFirestore) : LeaderboardRepository {

    override suspend fun getTopPlayersByLevel(
        selectedBadge: String,
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("users")
            .whereEqualTo("badge", selectedBadge)
            .orderBy("totalPoints", Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .addOnSuccessListener { snapshot ->
                val players = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(User::class.java)?.copy(id = doc.id)
                }
                onSuccess(players)
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is HttpRequestTimeoutException -> AppResult.Error(AppError.Timeout)
                    is ConnectTimeoutException -> AppResult.Error(AppError.NoInternetConnection)
                    is IOException -> AppResult.Error(AppError.Network)
                    is RedirectResponseException -> AppResult.Error(AppError.Server)
                    is ClientRequestException -> AppResult.Error(AppError.BadRequest)
                    is ServerResponseException -> AppResult.Error(AppError.ServerDown)
                    else -> AppResult.Error(AppError.Unknown)
                }
            }
    }

    override suspend fun getTopPlayersByCategory(
        selectedCategory: String,
        onSuccess: (List<Results>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collectionGroup("results")
            .whereEqualTo("category", selectedCategory)
            .orderBy("correctAnswers", Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .addOnSuccessListener { snapshot ->
                val categoryLeaders = snapshot.documents.mapNotNull { doc ->
                    val result = doc.toObject(Results::class.java)
                    val extractedUserId = doc.reference.parent.parent?.id ?: ""
                    result?.copy(userId = extractedUserId)
                }
                onSuccess(categoryLeaders)
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is HttpRequestTimeoutException -> AppResult.Error(AppError.Timeout)
                    is ConnectTimeoutException -> AppResult.Error(AppError.NoInternetConnection)
                    is IOException -> AppResult.Error(AppError.Network)
                    is RedirectResponseException -> AppResult.Error(AppError.Server)
                    is ClientRequestException -> AppResult.Error(AppError.BadRequest)
                    is ServerResponseException -> AppResult.Error(AppError.ServerDown)
                    else -> AppResult.Error(AppError.Unknown)
                }
            }
    }

    override suspend fun getPlayerAheadByCategory(
        selectedCategory: String,
        correctAnswers: Int,
        onSuccess: (Results?, Int?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collectionGroup("results")
            .whereEqualTo("category", selectedCategory)
            .whereGreaterThan("correctAnswers", correctAnswers)
            .orderBy("correctAnswers", Query.Direction.ASCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val resultAhead = snapshot.documents.firstOrNull()?.let { doc ->
                    val result = doc.toObject(Results::class.java)
                    val extractedUserId = doc.reference.parent.parent?.id ?: ""
                    result?.copy(userId = extractedUserId)
                }
                onSuccess(resultAhead, correctAnswers * 5)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    override suspend fun getPlayerBehindByCategory(
        selectedCategory: String,
        correctAnswers: Int,
        onSuccess: (Results?, Int?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collectionGroup("results")
            .whereEqualTo("category", selectedCategory)
            .whereLessThan("correctAnswers", correctAnswers)
            .orderBy("correctAnswers", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val resultAhead = snapshot.documents.firstOrNull()?.let { doc ->
                    val result = doc.toObject(Results::class.java)
                    val extractedUserId = doc.reference.parent.parent?.id ?: ""
                    result?.copy(userId = extractedUserId)
                }
                onSuccess(resultAhead, correctAnswers * 5)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}