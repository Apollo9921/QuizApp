package com.example.quizapp.domain.model.user

data class User(
    val name: String,
    val totalPoints: Int,
    val totalPointsPossible: Int,
    val badge: String
)