package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.presentation.screens.progress.ProgressViewModel

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