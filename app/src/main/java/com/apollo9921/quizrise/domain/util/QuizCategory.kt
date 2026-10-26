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

        fun getBadgeDrawableTop1ForCategory(categoryName: String): Int? {
            return when (categoryName) {
                ARTS_AND_LITERATURE.categoryName -> R.drawable.badge_arts_and_literature_top1
                FILM_AND_TV.categoryName -> R.drawable.badge_film_tv_top1
                FOOD_AND_DRINK.categoryName -> R.drawable.badge_food_drink_top1
                GENERAL_KNOWLEDGE.categoryName -> R.drawable.badge_general_knowledge_top1
                GEOGRAPHY.categoryName -> R.drawable.badge_geography_top1
                HISTORY.categoryName -> R.drawable.badge_history_top1
                MUSIC.categoryName -> R.drawable.badge_music_top1
                SCIENCE.categoryName -> R.drawable.badge_science_top1
                SOCIETY_AND_CULTURE.categoryName -> R.drawable.badge_society_culture_top1
                SPORT_AND_LEISURE.categoryName -> R.drawable.badge_sport_leisure_top1
                else -> null
            }
        }
    }
}