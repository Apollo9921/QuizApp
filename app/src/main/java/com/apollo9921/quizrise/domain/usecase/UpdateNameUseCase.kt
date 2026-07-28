package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult

class UpdateNameUseCase(
    private val userRepository: UserRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(
        name: String,
        oldName: String,
        results: List<Results>
    ): AppResult<Unit> {
        if (name.isEmpty()) {
            return AppResult.Error(AppError.EmptyFields)
        }

        if (name == oldName) {
            return AppResult.Error(AppError.SameName)
        }

        val userUpdate = userRepository.updateName(name, oldName)
        val resultsUpdate = resultsRepository.updateUsername(name, oldName)
        return if (userUpdate.isSuccess && resultsUpdate.isSuccess) {
            userRepository.postUserName(name, results)
        } else {
            AppResult.Error(AppError.Unknown)
        }
    }
}