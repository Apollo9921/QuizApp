package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository

class ClearAllDataUseCase(
    private val userRepository: UserRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke() {
        userRepository.clearAllData()
        resultsRepository.clearAllData()
    }
}