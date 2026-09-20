package com.apollo9921.quizrise.presentation

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apollo9921.quizrise.BuildConfig
import com.apollo9921.quizrise.domain.util.ConsentManager
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.dataStore.UserManager
import com.apollo9921.quizrise.presentation.dataStore.dataStoreUser
import com.apollo9921.quizrise.presentation.navigation.AnimationNav
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var appUpdateManager: AppUpdateManager
    private val consentManager by lazy { ConsentManager(this) }

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

    private var startDestination by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(installStateUpdatedListener)

        splashScreen.setKeepOnScreenCondition { startDestination == null }

        lifecycleScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            val userManager = UserManager(dataStore = dataStoreUser)
            val isOnboardingCompleted = userManager.userFlow.first()

            startDestination = when {
                user != null -> Destination.Categories.route
                !isOnboardingCompleted -> Destination.OnBoard.route
                else -> Destination.Login.route
            }
        }

        setContent {
            QuizAppTheme {
                startDestination?.let { destination ->
                    navHostController = rememberNavController()
                    AnimationNav(
                        navHostController = navHostController,
                        startDestination = destination
                    )

                    LaunchedEffect(Unit) {
                        checkForAppUpdate()

                        consentManager.gatherConsent(
                            testDeviceHashedId = if (BuildConfig.DEBUG) "MY_HASHED_DEVICE_ID" else null
                        ) { canRequestAds ->
                            if (canRequestAds) {
                                MobileAds.initialize(this@MainActivity)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::appUpdateManager.isInitialized) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackBarForCompleteUpdate()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::appUpdateManager.isInitialized) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
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
        AlertDialog.Builder(this)
            .setTitle("Update Completed")
            .setMessage("New version ready to install. Please restart the app.")
            .setPositiveButton("Restart Now") { _, _ ->
                appUpdateManager.completeUpdate()
            }
            .setCancelable(false)
            .show()
    }
}