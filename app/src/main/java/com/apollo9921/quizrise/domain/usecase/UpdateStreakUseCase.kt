package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class UpdateStreakUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Pair<Int, Int> {
        val lastLoginDateMs = user.lastLoginDate
        val lastLoginDate = Instant.ofEpochMilli(lastLoginDateMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(lastLoginDate, today)
        when {
            daysBetween == 1L -> {
                val newStreak = user.currentStreak + 1
                var newLives = user.currentLives

                if (newStreak % 7 == 0) {
                    newLives += 3
                } else if (newStreak % 3 == 0) {
                    newLives += 1
                }

                repository.updateStreak(
                    currentStreak = newStreak,
                    currentLives = newLives,
                    lastLoginDate = today.toEpochDay(),
                    name = user.name
                )

                return Pair(newStreak, newLives)
            }

            daysBetween > 1L -> {
                repository.updateStreak(
                    currentStreak = 1,
                    currentLives = 0,
                    lastLoginDate = today.toEpochDay(),
                    name = user.name
                )

                return Pair(1, 0)
            }

            else -> {
                return Pair(user.currentStreak, user.currentLives)
            }
        }
    }
}