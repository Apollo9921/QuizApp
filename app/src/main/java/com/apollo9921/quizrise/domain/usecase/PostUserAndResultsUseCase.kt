package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.domain.util.QuizCategory

class PostUserAndResultsUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String, session: String): AppResult<Unit> {
        val user = User(
            name = name,
            totalPoints = 0,
            totalPointsPossible = 0,
            badge = PlayerLevel.RECRUIT.badgeName,
            session = session
        )

        val resultsList = QuizCategory.entries.map { categoryRes ->
            Results(
                category = categoryRes.categoryName,
                correctAnswers = 0,
                incorrectAnswers = 0,
                username = name
            )
        }

        return repository.postUserAndResults(user, resultsList)
    }
}