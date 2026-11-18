package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.UserRepository

class UpdateStreakUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(currentStreak: Int, currentLives: Int, lastLoginDate: Long, name: String) {
        //TODO IMPLEMENT HERE THE LOGIC TO UPDATE THE STREAK AND LIVES THAT ARE ON CategoriesViewModel RIGHT NOW
        repository.updateStreak(
            currentStreak = currentStreak,
            currentLives = currentLives,
            lastLoginDate = lastLoginDate,
            name = name
        )
    }
}