package com.example.quizapp.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.result.AppResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateUserWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val repository: UserRepository by inject()

    override suspend fun doWork(): Result {
        val totalPoints = inputData.getInt("totalPoints", 0)
        val totalPointsPossible = inputData.getInt("totalPointsPossible", 0)
        val category = inputData.getString("category") ?: return Result.failure()
        val correct = inputData.getInt("correct", 0)
        val incorrect = inputData.getInt("incorrect", 0)

        val user = User(
            totalPoints = totalPoints,
            totalPointsPossible = totalPointsPossible,
            name = "",
            badge = ""
        )
        val results =
            Results(category = category, correctAnswers = correct, incorrectAnswers = incorrect)

        val appResult = repository.updateUserAndResults(user, results)

        return if (appResult is AppResult.Success) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}