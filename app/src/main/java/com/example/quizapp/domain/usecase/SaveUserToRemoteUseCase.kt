package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult
import com.example.quizapp.domain.util.PlayerLevel
import com.example.quizapp.domain.util.QuizCategory

class SaveUserToRemoteUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String): AppResult<Unit> {
        val user = User(
            name = name,
            totalPoints = 0,
            totalPointsPossible = 0,
            badge = PlayerLevel.RECRUIT.badgeName
        )

        val resultsList = QuizCategory.entries.map { categoryRes ->
            Results(
                category = categoryRes.categoryName,
                correctAnswers = 0,
                incorrectAnswers = 0,
                username = name
            )
        }

        return repository.saveUserAndResults(user, resultsList)
    }
}