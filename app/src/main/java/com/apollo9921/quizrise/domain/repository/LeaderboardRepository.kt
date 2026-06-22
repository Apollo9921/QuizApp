package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User

interface LeaderboardRepository {
    suspend fun getTopPlayersByLevel(selectedBadge: String, onSuccess: (List<User>) -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getTopPlayersByCategory(selectedCategory: String, onSuccess: (List<Results>) -> Unit, onFailure: (Exception) -> Unit)
}