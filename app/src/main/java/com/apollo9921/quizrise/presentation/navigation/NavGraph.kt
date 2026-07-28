package com.apollo9921.quizrise.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.apollo9921.quizrise.presentation.screens.progress.ProgressRoute
import com.apollo9921.quizrise.presentation.screens.categories.CategoriesRoute
import com.apollo9921.quizrise.presentation.screens.profile.ProfileRoute
import com.apollo9921.quizrise.presentation.screens.boarding.OnBoard
import com.apollo9921.quizrise.presentation.screens.deleteAccount.DeleteAccountRoute
import com.apollo9921.quizrise.presentation.screens.leaderboard.LeaderboardRoute
import com.apollo9921.quizrise.presentation.screens.login.LoginRoute
import com.apollo9921.quizrise.presentation.screens.quizLevel.LevelDifficulty
import com.apollo9921.quizrise.presentation.screens.quiz.StartQuizRoute
import com.apollo9921.quizrise.presentation.screens.quizResult.QuizResultRoute
import com.apollo9921.quizrise.presentation.screens.register.RegisterRoute
import com.apollo9921.quizrise.presentation.screens.results.ResultsRoute
import com.apollo9921.quizrise.presentation.screens.wrongAnswers.WrongAnswersRoute

@Composable
fun AnimationNav(navHostController: NavHostController, startDestination: String) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(
            route = Destination.OnBoard.route,
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 500, easing = EaseInOut))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            OnBoard(navHostController = navHostController)
        }
        composable(
            route = Destination.Login.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            LoginRoute(navHostController)
        }
        composable(
            route = Destination.Register.route,
            arguments = listOf(
                navArgument("isAnonymous") {
                    type = NavType.BoolType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            val isAnonymous = it.arguments?.getBoolean("isAnonymous")
            RegisterRoute(
                isAnonymous = isAnonymous == true,
                onNavigateBack = { navHostController.navigateUp() },
                navigateToCategories = { navHostController.navigate(Destination.Categories.route) }
            )
        }
        composable(
            route = Destination.Progress.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ProgressRoute(navHostController = navHostController)
        }
        composable(
            route = Destination.Categories.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            CategoriesRoute(navHostController = navHostController)
        }
        composable(
            route = Destination.Leaderboard.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            LeaderboardRoute(navHostController = navHostController)
        }
        composable(
            route = Destination.Results.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ResultsRoute(navHostController = navHostController)
        }
        composable(
            route = Destination.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ProfileRoute(navHostController = navHostController)
        }
        composable(
            route = Destination.DeleteAccount.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            DeleteAccountRoute(navHostController = navHostController)
        }
        composable(
            route = Destination.LevelDifficulty.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            LevelDifficulty(
                navHostController = navHostController,
                category = it.arguments?.getString("category")!!
            )
        }
        composable(
            route = Destination.StartQuiz.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                },
                navArgument("level") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            StartQuizRoute(
                navHostController = navHostController,
                category = it.arguments?.getString("category")!!,
                level = it.arguments?.getString("level")!!
            )
        }
        composable(
            route = Destination.QuizResult.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("correctAnswers") { type = NavType.IntType },
                navArgument("incorrectAnswers") { type = NavType.IntType },
                navArgument("question") {
                    type = NavType.StringArrayType
                    nullable = true
                },
                navArgument("answers") {
                    type = NavType.StringArrayType
                    nullable = true
                },
                navArgument("correctAnswersList") {
                    type = NavType.StringArrayType
                    nullable = true
                }
            ),
        ) {
            QuizResultRoute(
                navHostController = navHostController,
                category = it.arguments?.getString("category") ?: "",
                correctAnswers = it.arguments?.getInt("correctAnswers") ?: 0,
                incorrectAnswers = it.arguments?.getInt("incorrectAnswers") ?: 0,
                question = it.arguments?.getStringArray("question") ?: emptyArray(),
                answers = it.arguments?.getStringArray("answers") ?: emptyArray(),
                correctAnswersList = it.arguments?.getStringArray("correctAnswersList") ?: emptyArray()
            )
        }
        composable(
            route = Destination.WrongAnswers.route,
            arguments = listOf(
                navArgument("question") { type = NavType.StringArrayType },
                navArgument("correctAnswers") { type = NavType.StringArrayType },
                navArgument("incorrectAnswers") { type = NavType.StringArrayType }
            )
        ) { backStackEntry ->
            val question = backStackEntry.arguments?.getStringArray("question") ?: emptyArray()
            val incorrect = backStackEntry.arguments?.getStringArray("incorrectAnswers") ?: emptyArray()
            val correct = backStackEntry.arguments?.getStringArray("correctAnswers") ?: emptyArray()

            WrongAnswersRoute(
                navHostController = navHostController,
                question = question,
                incorrectAnswers = incorrect,
                correctAnswers = correct
            )
        }
    }
}