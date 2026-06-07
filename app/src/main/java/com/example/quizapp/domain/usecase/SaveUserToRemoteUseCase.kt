package com.example.quizapp.domain.usecase

import android.content.Context
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult
import com.example.quizapp.presentation.utils.badgesDescription
import com.example.quizapp.presentation.utils.categories

class SaveUserToRemoteUseCase(
    private val context: Context,
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String): AppResult<Unit> {
        val user = User(
            name = name,
            totalPoints = 0,
            totalPointsPossible = 0,
            badge = context.resources.getString(badgesDescription[0])
        )

        val resultsList = categories.map { categoryRes ->
            Results(
                category = context.resources.getString(categoryRes),
                correctAnswers = 0,
                incorrectAnswers = 0
            )
        }

        return repository.saveUserAndResults(user, resultsList)
    }
}