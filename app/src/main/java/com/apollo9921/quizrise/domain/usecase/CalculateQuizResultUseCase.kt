package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.PlayerLevel

class CalculateQuizResultUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(correctAnswers: Int): AppResult<Pair<Int, Int>> {
         try {
            val userResult = userRepository.fetchUser()
            if (userResult.isSuccess) {
                val userResult = userResult.getOrThrow()
                val totalPoints = userResult.totalPoints
                val pointsReceived = correctAnswers * 5
                val currentLevel = PlayerLevel.getLevelByPoints(totalPoints + pointsReceived)
                val pointsToNextLevel =
                    (currentLevel.maxPoints + 5) - (totalPoints + pointsReceived)
                return AppResult.Success(Pair(pointsReceived, pointsToNextLevel))
            } else {
                return AppResult.Error(AppError.Unknown)
            }
        } catch (_: Exception) {
            return AppResult.Error(AppError.Unknown)
        }
    }
}