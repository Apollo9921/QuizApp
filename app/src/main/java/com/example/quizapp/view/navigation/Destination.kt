package com.example.quizapp.view.navigation

sealed class Destination(val route: String) {
    data object OnBoard: Destination(route = "onboard")
    data object CreateUser: Destination(route = "create_user")
    data object Main: Destination(route = "main")
    data object Progress: Destination(route = "progress")
    data object Quiz: Destination(route = "quiz")
    data object Results: Destination(route = "results")
    data object Profile: Destination(route = "profile")
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