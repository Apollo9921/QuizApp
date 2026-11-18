package com.apollo9921.quizrise.domain.model.user

import androidx.annotation.Keep

@Keep
data class User(
    val id: String = "",
    val name: String = "",
    val totalPoints: Int = 0,
    val totalPointsPossible: Int = 0,
    val badge: String = "",
    val session: String = "",
    val currentStreak: Int = 0,
    val currentLives: Int = 0,
    val lastLoginDate: Long = 0
)