package com.example.quizapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.navigation.AnimationNav
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.QuizAppTheme
import com.google.firebase.auth.FirebaseAuth

var isSplashScreenOpen = true

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            isSplashScreenOpen
        }
        setContent {
            QuizAppTheme {
                val user = FirebaseAuth.getInstance().currentUser
                navHostController = rememberNavController()
                val startDestination =
                    if (user != null) Destination.Categories.route else Destination.OnBoard.route
                AnimationNav(
                    navHostController = navHostController,
                    startDestination = startDestination
                )
            }
        }
    }
}