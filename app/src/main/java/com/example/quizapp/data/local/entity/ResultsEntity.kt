package com.example.quizapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class ResultsEntity(
    @PrimaryKey(autoGenerate = false)
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
)