package com.example.quizapp.view.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.quizapp.view.Main
import com.example.quizapp.view.createUser.CreateUser
import com.example.quizapp.view.onBoard.OnBoard
import com.example.quizapp.view.quiz.LevelDifficulty
import com.example.quizapp.view.quiz.QuizResult
import com.example.quizapp.view.quiz.StartQuiz
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationNav(navHostController: NavHostController) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = Destination.OnBoard.route
    ) {
        composable(
            route = Destination.OnBoard.route,
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 500, easing = EaseInOut))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            OnBoard(navHostController = navHostController)
        }
        composable(
            route = Destination.CreateUser.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            CreateUser(navHostController = navHostController)
        }
        composable(
            route = Destination.Main.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            Main(navHostController = navHostController)
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
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
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
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            StartQuiz(
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
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            QuizResult(
                navHostController = navHostController,
                category = it.arguments?.getString("category")!!,
                correctAnswers = it.arguments?.getInt("correctAnswers")!!,
                incorrectAnswers = it.arguments?.getInt("incorrectAnswers")!!
            )
        }
    }
}