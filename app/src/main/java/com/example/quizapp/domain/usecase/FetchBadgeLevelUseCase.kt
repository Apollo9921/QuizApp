package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.presentation.utils.badgesPoints

class FetchBadgeLevelUseCase {
    operator fun invoke(data: UserEntity): Int {
        for (i in badgesPoints.indices) {
            if (data.totalPoints <= badgesPoints[i]) {
                return badgesPoints[i]
                break
            }
        }
        return 0
    }
}