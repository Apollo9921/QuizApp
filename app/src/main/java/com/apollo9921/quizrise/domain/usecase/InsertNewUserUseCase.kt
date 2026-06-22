package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.util.PlayerLevel

class InsertNewUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String) {
        val user = User(
            name = name,
            totalPoints = 0,
            totalPointsPossible = 0,
            badge = PlayerLevel.RECRUIT.badgeName
        )
        repository.insertUser(user)
    }
}