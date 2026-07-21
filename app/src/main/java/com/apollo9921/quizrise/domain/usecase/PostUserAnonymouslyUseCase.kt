package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.domain.util.QuizCategory

class PostUserAnonymouslyUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(name: String, session: String): AppResult<Unit> {
        val result = authRepository.signInAnonymously()
        if (result is AppResult.Success) {
            val user = User(
                name = name,
                totalPoints = 0,
                totalPointsPossible = 0,
                badge = PlayerLevel.RECRUIT.badgeName,
                session = session
            )

            QuizCategory.entries.map { categoryRes ->
                val results = Results(
                    category = categoryRes.categoryName,
                    correctAnswers = 0,
                    incorrectAnswers = 0,
                    username = name
                )
                resultsRepository.insertResults(results)
            }

            val resultsList = QuizCategory.entries.map { categoryRes ->
                Results(
                    category = categoryRes.categoryName,
                    correctAnswers = 0,
                    incorrectAnswers = 0,
                    username = name
                )
            }

            userRepository.insertUser(user)
            val response = userRepository.postUserAndResults(user, resultsList)
            return if (response is AppResult.Success) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error((response as AppResult.Error).error)
            }
        } else {
            return AppResult.Error((result as AppResult.Error).error)
        }
    }
}