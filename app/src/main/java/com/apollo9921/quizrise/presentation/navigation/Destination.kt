package com.apollo9921.quizrise.presentation.navigation

sealed class Destination(val route: String) {
    data object OnBoard: Destination(route = "onboard")
    data object Login: Destination(route = "login")
    data object Register: Destination(route = "register/{isAnonymous}") {
        fun passArgument(isAnonymous: Boolean): String {
            return "register/$isAnonymous"
        }
    }
    data object Progress: Destination(route = "progress")
    data object Categories: Destination(route = "categories")
    data object Leaderboard: Destination(route = "leaderboard")
    data object Results: Destination(route = "results")
    data object Profile: Destination(route = "profile")
    data object EditUserName: Destination(route = "edit_username")
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
    data object QuizResult : Destination(route = "quiz_result/{category}/{correctAnswers}/{incorrectAnswers}?question={question}&answers={answers}&correctAnswersList={correctAnswersList}") {
        fun passArgument(
            category: String,
            correctAnswers: Int,
            incorrectAnswers: Int,
            question: List<String>,
            answers: List<String>,
            correctAnswersList: List<String>
        ): String {
            val basePath = "quiz_result/$category/$correctAnswers/$incorrectAnswers"

            val qParams = question.joinToString("&") { "question=$it" }
            val aParams = answers.joinToString("&") { "answers=$it" }
            val cParams = correctAnswersList.joinToString("&") { "correctAnswersList=$it" }

            return "$basePath?$qParams&$aParams&$cParams"
        }
    }

    data object WrongAnswers : Destination(route = "wrong_answers?question={question}&correctAnswers={correctAnswers}&incorrectAnswers={incorrectAnswers}") {
        fun passArgument(
            question: Array<String>,
            correctAnswers: Array<String>,
            incorrectAnswers: Array<String>
        ): String {
            val qParams = question.joinToString("&") { "question=$it" }
            val cParams = correctAnswers.joinToString("&") { "correctAnswers=$it" }
            val iParams = incorrectAnswers.joinToString("&") { "incorrectAnswers=$it" }

            return "wrong_answers?$qParams&$cParams&$iParams"
        }
    }
}