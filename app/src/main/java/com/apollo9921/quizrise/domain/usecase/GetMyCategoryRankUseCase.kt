package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.QuizCategory
import com.apollo9921.quizrise.presentation.components.Achievement

class GetMyCategoryRankUseCase(
    private val userRepository: UserRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(
        onSuccess: (Achievement?) -> Unit,
        onFailure: (AppResult<Unit>) -> Unit
    ) {
        val currentUserCorrectAnswers = resultsRepository.fetchResults()
        if (currentUserCorrectAnswers.isSuccess) {
            val results = currentUserCorrectAnswers.getOrThrow()
            results.forEach { result ->
                userRepository.getMyCategoryRank(
                    selectedCategory = result.category,
                    currentUserCorrectAnswers = result.correctAnswers,
                    onSuccess = { rank ->
                        if (rank > 1) {
                            onSuccess(null)
                        } else {
                            val categoryAchievement =
                                QuizCategory.getBadgeDrawableTop1ForCategory(result.category)
                            if (categoryAchievement != null) {
                                val achievement = Achievement(
                                    title = result.category,
                                    iconRes = categoryAchievement
                                )
                                onSuccess(achievement)
                            } else {
                                onSuccess(null)
                            }
                        }
                    },
                    onFailure = { _ ->
                        onFailure(AppResult.Error(AppError.Unknown))
                    }
                )
            }
        } else {
            onFailure(AppResult.Error(AppError.Unknown))
        }
    }
}