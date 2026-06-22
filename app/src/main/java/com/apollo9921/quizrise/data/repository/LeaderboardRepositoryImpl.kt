package com.apollo9921.quizrise.data.repository

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.LeaderboardRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException

class LeaderboardRepositoryImpl(
    private val firestore: FirebaseFirestore
): LeaderboardRepository {

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
                when(exception) {
                    is HttpRequestTimeoutException -> AppError.Timeout
                    is ConnectTimeoutException -> AppError.NoInternetConnection
                    is IOException -> AppError.Network
                    is RedirectResponseException -> AppError.Server
                    is ClientRequestException -> AppError.BadRequest
                    is ServerResponseException -> AppError.ServerDown
                    else -> AppError.Unknown
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
                when(exception) {
                    is HttpRequestTimeoutException -> AppError.Timeout
                    is ConnectTimeoutException -> AppError.NoInternetConnection
                    is IOException -> AppError.Network
                    is RedirectResponseException -> AppError.Server
                    is ClientRequestException -> AppError.BadRequest
                    is ServerResponseException -> AppError.ServerDown
                    else -> AppError.Unknown
                }
            }
    }
}