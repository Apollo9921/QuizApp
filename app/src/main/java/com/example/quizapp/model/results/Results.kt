package com.example.quizapp.model.results

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class Results(
    @PrimaryKey(autoGenerate = false)
    val category: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int
)