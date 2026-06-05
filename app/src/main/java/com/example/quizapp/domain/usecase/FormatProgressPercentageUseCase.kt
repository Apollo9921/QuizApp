package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.presentation.screens.progress.ProgressViewModel
import com.example.quizapp.presentation.utils.badgesPoints

class FormatProgressPercentageUseCase {
    operator fun invoke(data: UserEntity): ProgressViewModel.UserData {
        val userData = ProgressViewModel.UserData(totalPoints = data.totalPoints)
        for (i in badgesPoints.indices) {
            if (data.totalPoints <= badgesPoints[i]) {
                userData.badge = badgesPoints[i]
                break
            }
        }
        userData.percentage = (data.totalPoints * 100) / userData.badge.toDouble()
        userData.percentage *= 0.01
        return userData
    }
}