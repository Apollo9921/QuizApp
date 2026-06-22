package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository

class FetchUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.fetchUser()
    }
}