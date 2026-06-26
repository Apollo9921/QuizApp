package com.apollo9921.quizrise.domain.usecase

import android.content.Context
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.model.quiz.Quiz
import com.apollo9921.quizrise.domain.repository.QuizRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.QuizConstants

class GetQuizUseCase(
    private val repository: QuizRepository,
    private val context: Context
) {
    suspend operator fun invoke(categoryResId: String, levelResId: String, session: String): AppResult<List<Quiz>> {
        val categoryKey = when (categoryResId) {
            context.getString(R.string.artsAndLiterature_translatable) -> QuizConstants.CATEGORY_ARTS_LITERATURE
            context.getString(R.string.science_translatable) -> QuizConstants.CATEGORY_SCIENCE
            context.getString(R.string.geography_translatable) -> QuizConstants.CATEGORY_GEOGRAPHY
            context.getString(R.string.history_translatable) -> QuizConstants.CATEGORY_HISTORY
            context.getString(R.string.music_translatable) -> QuizConstants.CATEGORY_MUSIC
            context.getString(R.string.filmAndTV_translatable) -> QuizConstants.CATEGORY_FILM_TV
            context.getString(R.string.foodAndDrink_translatable) -> QuizConstants.CATEGORY_FOOD_DRINK
            context.getString(R.string.generalKnowledge_translatable) -> QuizConstants.CATEGORY_GENERAL_KNOWLEDGE
            context.getString(R.string.societyAndCulture_translatable) -> QuizConstants.CATEGORY_SOCIETY_CULTURE
            context.getString(R.string.sportAndLeisure_translatable) -> QuizConstants.CATEGORY_SPORT_LEISURE
            else -> return AppResult.Error(AppError.NoCategoryOrLevelDefined)
        }

        val levelKey = when (levelResId) {
            context.getString(R.string.easy_translatable) -> QuizConstants.LEVEL_EASY
            context.getString(R.string.medium_translatable) -> QuizConstants.LEVEL_MEDIUM
            context.getString(R.string.hard_translatable) -> QuizConstants.LEVEL_HARD
            else -> return AppResult.Error(AppError.NoCategoryOrLevelDefined)
        }

        return repository.getQuiz(categoryKey, levelKey, session)
    }
}