package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.presentation.utils.badgesPoints

class FetchBadgeLevelUseCase {
    operator fun invoke(data: User): Int {
        for (i in badgesPoints.indices) {
            if (data.totalPoints <= badgesPoints[i]) {
                return badgesPoints[i]
                break
            }
        }
        return 0
    }
}