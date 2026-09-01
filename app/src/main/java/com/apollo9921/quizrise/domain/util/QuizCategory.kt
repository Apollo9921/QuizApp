package com.apollo9921.quizrise.domain.util

import com.apollo9921.quizrise.R

enum class QuizCategory(
    val categoryName: String,
    val resourceId: Int
) {
    ARTS_AND_LITERATURE("Arts and Literature", R.string.artsAndLiterature_translatable),
    FILM_AND_TV("Film and TV", R.string.filmAndTV_translatable),
    FOOD_AND_DRINK("Food and Drink", R.string.foodAndDrink_translatable),
    GENERAL_KNOWLEDGE("General Knowledge", R.string.generalKnowledge_translatable),
    GEOGRAPHY("Geography", R.string.geography_translatable),
    HISTORY("History", R.string.history_translatable),
    MUSIC("Music", R.string.music_translatable),
    SCIENCE("Science", R.string.science_translatable),
    SOCIETY_AND_CULTURE("Society and Culture", R.string.societyAndCulture_translatable),
    SPORT_AND_LEISURE("Sport and Leisure", R.string.sportAndLeisure_translatable);

    companion object {
        fun getLevelByName(resourceId: Int): String {
            return QuizCategory.entries.firstOrNull { it.resourceId == resourceId }?.categoryName ?: ARTS_AND_LITERATURE.categoryName
        }

        fun getResourceByName(name: String): Int {
            return QuizCategory.entries.firstOrNull { it.categoryName == name }?.resourceId ?: ARTS_AND_LITERATURE.resourceId
        }
    }
}