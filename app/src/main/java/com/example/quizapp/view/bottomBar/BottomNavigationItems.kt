package com.example.quizapp.view.bottomBar

import com.example.quizapp.R
import com.example.quizapp.view.navigation.Destination

sealed class BottomNavigationItems(val title: Int, var icon: Int, var route: String) {
    object Progress : BottomNavigationItems(
        R.string.progress,
        R.drawable.daily,
        Destination.Progress.route
    )

    object Quiz : BottomNavigationItems(R.string.quiz, R.drawable.quiz, Destination.Quiz.route)

    object Results :
        BottomNavigationItems(R.string.results, R.drawable.results, Destination.Results.route)

    object Profile :
        BottomNavigationItems(R.string.profile, R.drawable.profile, Destination.Profile.route)
}
