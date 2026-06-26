package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(): AppResult<Unit> {
        try {
            userRepository.clearAllData()
            resultsRepository.clearAllData()
            return userRepository.deleteAccount()
        } catch (_: Exception) {
            return AppResult.Error(AppError.Unknown)
        }
    }
}