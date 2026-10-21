package com.apollo9921.quizrise.domain.usecase

import android.content.Context
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.repository.LeaderboardRepository
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.NetworkHelper
import com.apollo9921.quizrise.domain.util.QuizCategory

class GetPlayerAheadByCategoryUseCase(
    private val resultsRepository: ResultsRepository,
    private val leaderboardRepository: LeaderboardRepository,
    private val context: Context
) {
    suspend operator fun invoke(
        selectedCategory: String,
        onSuccess: (Results?, Int?) -> Unit,
        onFailure: (AppResult<Unit>) -> Unit
    ) {
        val results = resultsRepository.fetchResults()
        if (results.isSuccess) {
            val categoryKey = when (selectedCategory) {
                context.getString(R.string.artsAndLiterature_translatable) -> QuizCategory.ARTS_AND_LITERATURE.categoryName
                context.getString(R.string.science_translatable) -> QuizCategory.SCIENCE.categoryName
                context.getString(R.string.geography_translatable) -> QuizCategory.GEOGRAPHY.categoryName
                context.getString(R.string.history_translatable) -> QuizCategory.HISTORY.categoryName
                context.getString(R.string.music_translatable) -> QuizCategory.MUSIC.categoryName
                context.getString(R.string.filmAndTV_translatable) -> QuizCategory.FILM_AND_TV.categoryName
                context.getString(R.string.foodAndDrink_translatable) -> QuizCategory.FOOD_AND_DRINK.categoryName
                context.getString(R.string.generalKnowledge_translatable) -> QuizCategory.GENERAL_KNOWLEDGE.categoryName
                context.getString(R.string.societyAndCulture_translatable) -> QuizCategory.SOCIETY_AND_CULTURE.categoryName
                context.getString(R.string.sportAndLeisure_translatable) -> QuizCategory.SPORT_AND_LEISURE.categoryName
                else -> QuizCategory.ARTS_AND_LITERATURE.categoryName
            }
            val resultByCategory = results.getOrThrow().first { it.category == categoryKey }
            val currentUserCorrectAnswers = resultByCategory.correctAnswers
            val networkHelper = NetworkHelper(context)
            leaderboardRepository.getPlayerAheadByCategory(
                networkHelper,
                categoryKey,
                currentUserCorrectAnswers,
                onSuccess,
                onFailure
            )
        } else {
            onFailure(AppResult.Error(AppError.Unknown))
        }
    }
}