package com.example.quizapp.view.navigation

sealed class Destination(val route: String) {
    object OnBoard: Destination(route = "onboard")
    object CreateUser: Destination(route = "create_user")
    object Main: Destination(route = "main")
    object Progress: Destination(route = "progress")
    object Quiz: Destination(route = "quiz")
    object Results: Destination(route = "results")
    object Profile: Destination(route = "profile")
    object LevelDifficulty: Destination(route = "level_difficulty/{category}") {
        fun passArgument(category: String): String {
            return "level_difficulty/$category"
        }
    }
    object StartQuiz: Destination(route = "start_quiz/{category}/{level}") {
        fun passArgument(category: String, level: String): String {
            return "start_quiz/$category/$level"
        }
    }
    object QuizResult: Destination(route = "quiz_result/{category}/{correctAnswers}/{incorrectAnswers}") {
        fun passArgument(category: String, correctAnswers: Int, incorrectAnswers: Int): String {
            return "quiz_result/$category/$correctAnswers/$incorrectAnswers"
        }
    }
}