package com.apollo9921.quizrise.domain.result

sealed interface AppError {
    data object Network : AppError

    data object Timeout : AppError

    data object Unauthorized : AppError

    data object Server : AppError

    data object ServerDown : AppError

    data object BadRequest : AppError

    data object NoInternetConnection : AppError

    data object EmptyFields : AppError

    data object InvalidEmailFormat : AppError

    data object PasswordLength : AppError

    data object PasswordMismatch : AppError

    data object InvalidCredentials : AppError

    data object NoCategoryOrLevelDefined : AppError

    data object UserNotFound : AppError

    data object UserAlreadyExists : AppError

    data object AnonymousUserExpiredQuiz : AppError

    data object SameName : AppError

    data object Unknown : AppError
}