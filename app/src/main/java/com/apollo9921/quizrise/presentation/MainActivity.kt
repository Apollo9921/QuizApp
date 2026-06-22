package com.apollo9921.quizrise.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apollo9921.quizrise.presentation.navigation.AnimationNav
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.google.firebase.auth.FirebaseAuth

var isSplashScreenOpen = true

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FoldDebug", "onCreate called, savedInstanceState=$savedInstanceState")
        installSplashScreen().setKeepOnScreenCondition {
            isSplashScreenOpen
        }
        setContent {
            QuizAppTheme {
                navHostController = rememberNavController()
                val startDestination = remember {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) Destination.Categories.route else Destination.OnBoard.route
                }
                AnimationNav(
                    navHostController = navHostController,
                    startDestination = startDestination
                )
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("FoldDebug", "onConfigurationChanged called, newConfig=$newConfig")
    }
}