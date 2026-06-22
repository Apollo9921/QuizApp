package com.apollo9921.quizrise.domain.util

import androidx.annotation.StringRes
import com.apollo9921.quizrise.R

enum class QuizCategory(
    val categoryName: String,
    @StringRes val resourceId: Int
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
        fun fromName(name: String): QuizCategory? =
            entries.find { it.categoryName == name }
    }
}