package com.apollo9921.quizrise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val totalPoints: Int,
    val totalPointsPossible: Int,
    val badge: String,
    val session: String
)