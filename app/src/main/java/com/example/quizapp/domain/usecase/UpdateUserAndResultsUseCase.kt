package com.example.quizapp.domain.usecase

import android.content.Context
import com.example.quizapp.R
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult
import com.example.quizapp.domain.util.QuizCategory

class UpdateUserAndResultsUseCase(
    private val context: Context,
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User, results: Results): AppResult<Unit> {
        val categoryName = when (results.category) {
            context.getString(R.string.artsAndLiterature_translatable) -> QuizCategory.ARTS_AND_LITERATURE.categoryName
            context.getString(R.string.filmAndTV_translatable) -> QuizCategory.FILM_AND_TV.categoryName
            context.getString(R.string.foodAndDrink_translatable) -> QuizCategory.FOOD_AND_DRINK.categoryName
            context.getString(R.string.generalKnowledge_translatable) -> QuizCategory.GENERAL_KNOWLEDGE.categoryName
            context.getString(R.string.geography_translatable) -> QuizCategory.GEOGRAPHY.categoryName
            context.getString(R.string.history_translatable) -> QuizCategory.HISTORY.categoryName
            context.getString(R.string.music_translatable) -> QuizCategory.MUSIC.categoryName
            context.getString(R.string.science_translatable) -> QuizCategory.SCIENCE.categoryName
            context.getString(R.string.societyAndCulture_translatable) -> QuizCategory.SOCIETY_AND_CULTURE.categoryName
            context.getString(R.string.sportAndLeisure_translatable) -> QuizCategory.SPORT_AND_LEISURE.categoryName
            else -> results.category
        }

        val updatedResults = results.copy(category = categoryName)
        return repository.updateUserAndResults(user, updatedResults)
    }
}