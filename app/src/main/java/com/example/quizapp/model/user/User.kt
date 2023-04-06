package com.example.quizapp.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val totalPoints: Int,
    val totalPointsPossible: Int,
    val badge: String
)
