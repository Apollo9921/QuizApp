package com.example.quizapp.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.view.dataStore.UserManager
import com.example.quizapp.view.dataStore.dataStoreUser
import com.example.quizapp.view.navigation.AnimationNav
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.QuizAppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

var isSplashScreenOpen = true
lateinit var userManager: UserManager
private var isLoaded = false
var userName = ""

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            isSplashScreenOpen
        }
        setContent {
            QuizAppTheme {
                userManager = UserManager(dataStore = LocalContext.current.dataStoreUser)
                navHostController = rememberNavController()
                AnimationNav(navHostController = navHostController)
                runBlocking { isLoaded = userManager.userFlow.first() }
                runBlocking { userName = userManager.userName.first().toString() }
                if (isLoaded && userName.isNotBlank()) {
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.Main.route)
                }
            }
        }
    }
}