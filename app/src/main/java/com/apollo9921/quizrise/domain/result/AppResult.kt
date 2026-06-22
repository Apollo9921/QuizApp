package com.apollo9921.quizrise.domain.result

sealed class AppResult<out T> {
    data class Success<T>(
        val data: T
    ) : AppResult<T>()

    data class Error(
        val error: AppError
    ) : AppResult<Nothing>()
}