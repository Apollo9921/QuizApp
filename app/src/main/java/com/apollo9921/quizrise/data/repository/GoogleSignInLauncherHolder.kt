package com.apollo9921.quizrise.data.repository

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CancellableContinuation

class GoogleSignInLauncherHolder {

    private var launcher: ActivityResultLauncher<Intent>? = null
    private var continuation: CancellableContinuation<AppResult<String>>? = null

    fun registerLauncher(launcher: ActivityResultLauncher<Intent>) {
        this.launcher = launcher
    }

    fun clearLauncher() {
        this.launcher = null
    }

    fun launch(
        intent: Intent,
        continuation: CancellableContinuation<AppResult<String>>
    ) {
        this.continuation = continuation
        val activeLauncher = launcher
        if (activeLauncher == null) {
            continuation.resumeWith(Result.success(AppResult.Error(AppError.Unknown)))
            return
        }
        activeLauncher.launch(intent)
    }

    fun handleResult(resultCode: Int, data: Intent?) {
        val pendingContinuation = continuation ?: return
        continuation = null

        if (resultCode != Activity.RESULT_OK || data == null) {
            pendingContinuation.resumeWith(Result.success(AppResult.Error(AppError.InvalidCredentials)))
            return
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                pendingContinuation.resumeWith(Result.success(AppResult.Success(idToken)))
            } else {
                pendingContinuation.resumeWith(Result.success(AppResult.Error(AppError.InvalidCredentials)))
            }
        } catch (_: com.google.android.gms.common.api.ApiException) {
            pendingContinuation.resumeWith(Result.success(AppResult.Error(AppError.InvalidCredentials)))
        }
    }
}