package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult

class UpdateUserToRemoteUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User, results: Results): AppResult<Unit> {
        return repository.updateUserAndResults(user, results)
    }
}