package com.example.quizapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.dataStore.UserManager
import com.example.quizapp.presentation.dataStore.dataStoreUser
import com.example.quizapp.presentation.navigation.AnimationNav
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.QuizAppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

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
                var isLoaded = false
                var userName = ""
                val userManager = UserManager(dataStore = LocalContext.current.dataStoreUser)
                runBlocking { isLoaded = userManager.userFlow.first() }
                runBlocking { userName = userManager.userName.first().toString() }
                navHostController = rememberNavController()
                if (isLoaded && userName.isNotBlank()) {
                    AnimationNav(
                        navHostController = navHostController,
                        startDestination = Destination.Categories.route
                    )
                } else {
                    AnimationNav(
                        navHostController = navHostController,
                        startDestination = Destination.OnBoard.route
                    )
                }
            }
        }
    }
}