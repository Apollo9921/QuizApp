package com.example.quizapp.domain.model.user

import androidx.annotation.Keep

@Keep
data class User(
    val id: String = "",
    val name: String = "",
    val totalPoints: Int = 0,
    val totalPointsPossible: Int = 0,
    val badge: String = ""
)