package com.apollo9921.quizrise.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apollo9921.quizrise.presentation.navigation.AnimationNav
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.dataStore.UserManager
import com.apollo9921.quizrise.presentation.dataStore.dataStoreUser
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

var isSplashScreenOpen = true

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var appUpdateManager: AppUpdateManager

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForCompleteUpdate()
        }
    }

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(installStateUpdatedListener)

        checkForAppUpdate()

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

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    private fun checkForAppUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            }
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Toast.makeText(this, "New Version Updated. Restarting...", Toast.LENGTH_LONG).show()
        appUpdateManager.completeUpdate()
    }
}