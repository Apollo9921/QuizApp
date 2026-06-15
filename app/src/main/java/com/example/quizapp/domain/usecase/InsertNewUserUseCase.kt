package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.util.PlayerLevel

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