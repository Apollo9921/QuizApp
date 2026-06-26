package com.apollo9921.quizrise.presentation.navigation

sealed class Destination(val route: String) {
    data object OnBoard: Destination(route = "onboard")
    data object Login: Destination(route = "login")
    data object Register: Destination(route = "register")
    data object Progress: Destination(route = "progress")
    data object Categories: Destination(route = "categories")
    data object Leaderboard: Destination(route = "leaderboard")
    data object Results: Destination(route = "results")
    data object Profile: Destination(route = "profile")
    data object DeleteAccount: Destination(route = "delete_account")
    data object LevelDifficulty: Destination(route = "level_difficulty/{category}") {
        fun passArgument(category: String): String {
            return "level_difficulty/$category"
        }
    }
    data object StartQuiz: Destination(route = "start_quiz/{category}/{level}") {
        fun passArgument(category: String, level: String): String {
            return "start_quiz/$category/$level"
        }
    }
    data object QuizResult: Destination(route = "quiz_result/{category}/{correctAnswers}/{incorrectAnswers}") {
        fun passArgument(category: String, correctAnswers: Int, incorrectAnswers: Int): String {
            return "quiz_result/$category/$correctAnswers/$incorrectAnswers"
        }
    }
}