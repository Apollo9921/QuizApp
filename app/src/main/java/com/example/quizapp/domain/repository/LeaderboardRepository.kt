package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User

interface LeaderboardRepository {
    suspend fun getTopPlayersByLevel(selectedBadge: String, onSuccess: (List<User>) -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getTopPlayersByCategory(selectedCategory: String, onSuccess: (List<Results>) -> Unit, onFailure: (Exception) -> Unit)
}