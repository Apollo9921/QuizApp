package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.NetworkHelper

interface LeaderboardRepository {
    suspend fun getTopPlayersByLevel(selectedBadge: String, onSuccess: (List<User>) -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getTopPlayersByCategory(selectedCategory: String, onSuccess: (List<Results>) -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getPlayerAheadByCategory(networkHelper: NetworkHelper, selectedCategory: String, correctAnswers: Int, onSuccess: (Results?, Int?) -> Unit, onFailure: (AppResult<Unit>) -> Unit)
    suspend fun getPlayerBehindByCategory(networkHelper: NetworkHelper, selectedCategory: String, correctAnswers: Int, onSuccess: (Results?, Int?) -> Unit, onFailure: (AppResult<Unit>) -> Unit)
}