package com.apollo9921.quizrise.domain.usecase

import android.content.Context
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.domain.util.QuizCategory
import com.apollo9921.quizrise.presentation.dataStore.UserManager
import com.apollo9921.quizrise.presentation.dataStore.dataStoreUser
import com.google.firebase.auth.FirebaseAuth
import kotlin.getOrThrow

class SaveQuizUseCase(
    private val context: Context,
    private val resultsRepository: ResultsRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(
        category: String,
        correctAnswers: Int,
        incorrectAnswers: Int
    ): AppResult<Unit> {
        try {
            val pointsPossible = 25

            val categoryName = when (category) {
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
                else -> category
            }

            val userResult = userRepository.fetchUser()
            if (userResult.isSuccess) {
                val userResult = userResult.getOrThrow()
                val badge = PlayerLevel.getLevelByPoints(userResult.totalPoints).badgeName
                val userName = userResult.name
                val pointsReceived = correctAnswers * 5

                resultsRepository.updateResults(categoryName, correctAnswers, incorrectAnswers)
                resultsRepository.updatePoints(
                    pointsReceived,
                    pointsPossible,
                    userName
                )
                userRepository.updateBadge(userResult.badge, userName)

                val userRemote = User("", userResult.name, pointsReceived, pointsPossible, badge)
                val resultsRemote = Results("", categoryName, correctAnswers, incorrectAnswers)

                val authUser = firebaseAuth.currentUser
                if (authUser != null) {
                    if (authUser.isAnonymous) {
                        val userManager = UserManager(dataStore = context.dataStoreUser)
                        userManager.updateQuizAllowed()
                    }
                }

                return userRepository.updateUserAndResults(userRemote, resultsRemote)
            } else {
                return AppResult.Error(AppError.Unknown)
            }
        } catch (_: Exception) {
            return AppResult.Error(AppError.Unknown)
        }
    }
}