package com.example.quizapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizapp.presentation.screens.progress.ProgressRoute
import com.example.quizapp.presentation.screens.categories.CategoriesRoute
import com.example.quizapp.presentation.screens.profile.ProfileRoute
import com.example.quizapp.presentation.screens.results.Results
import com.example.quizapp.presentation.screens.boarding.OnBoard
import com.example.quizapp.presentation.screens.login.LoginRoute
import com.example.quizapp.presentation.screens.quizLevel.LevelDifficulty
import com.example.quizapp.presentation.screens.quiz.StartQuizRoute
import com.example.quizapp.presentation.screens.quizResult.QuizResultRoute
import com.example.quizapp.presentation.screens.register.RegisterRoute

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
            RegisterRoute(onNavigateBack = { navHostController.navigateUp() })
        }
        composable(route = Destination.Progress.route) {
            ProgressRoute(navHostController = navHostController)
        }
        composable(route = Destination.Categories.route) {
            CategoriesRoute(navHostController = navHostController)
        }
        composable(route = Destination.Results.route) {
            Results(navHostController = navHostController)
        }
        composable(route = Destination.Profile.route) {
            ProfileRoute(navHostController = navHostController)
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
                navArgument("category") {
                    type = NavType.StringType
                },
                navArgument("correctAnswers") {
                    type = NavType.IntType
                },
                navArgument("incorrectAnswers") {
                    type = NavType.IntType
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
            QuizResultRoute(
                navHostController = navHostController,
                category = it.arguments?.getString("category")!!,
                correctAnswers = it.arguments?.getInt("correctAnswers")!!,
                incorrectAnswers = it.arguments?.getInt("incorrectAnswers")!!
            )
        }
    }
}