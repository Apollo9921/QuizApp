package com.apollo9921.quizrise.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apollo9921.quizrise.presentation.navigation.AnimationNav
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.dataStore.UserManager
import com.apollo9921.quizrise.presentation.dataStore.dataStoreUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

var isSplashScreenOpen = true

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                navHostController = rememberNavController()
                val startDestination = remember {
                    val user = FirebaseAuth.getInstance().currentUser
                    var isLoaded = false
                    val userManager = UserManager(dataStore = dataStoreUser)
                    runBlocking { isLoaded = userManager.userFlow.first() }
                    if (user != null) {
                        Destination.Categories.route
                    } else if (!isLoaded) {
                        Destination.OnBoard.route
                    } else {
                        Destination.Login.route
                    }
                }
                AnimationNav(
                    navHostController = navHostController,
                    startDestination = startDestination
                )
            }
        }
    }
}