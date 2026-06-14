package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.util.PlayerLevel
import com.example.quizapp.presentation.screens.progress.ProgressViewModel

class FormatProgressPercentageUseCase {
    operator fun invoke(data: User): ProgressViewModel.UserData {
        val maxPoints = PlayerLevel.getLevelByPoints(data.totalPoints).maxPoints
        var percentage = (data.totalPoints * 100) / maxPoints.toDouble()
        percentage *= 0.01
        return ProgressViewModel.UserData(
            currentPoints = data.totalPoints,
            maxPoints = maxPoints,
            percentage = percentage
        )
    }
}