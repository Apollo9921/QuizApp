package com.example.quizapp.domain.result

sealed class AppResult<out T> {
    data class Success<out T>(val data: T): AppResult<T>()
    data class Error<out T>(val message: T): AppResult<Nothing>()
}