package com.apollo9921.quizrise.presentation.navigation

import com.apollo9921.quizrise.R

sealed class BottomNavigationItems(val title: Int, var icon: Int, var route: String) {
    object Progress : BottomNavigationItems(
        R.string.progress,
        R.drawable.daily,
        Destination.Progress.route
    )

    object Quiz : BottomNavigationItems(R.string.quiz, R.drawable.quiz, Destination.Categories.route)

    object Results :
        BottomNavigationItems(R.string.results, R.drawable.results, Destination.Results.route)

    object Profile :
        BottomNavigationItems(R.string.profile, R.drawable.profile, Destination.Profile.route)
}