package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.presentation.utils.badges
import com.example.quizapp.presentation.utils.badgesPoints

class FetchBadgeUseCase {
    operator fun invoke(data: User, badge: Int): Int {
        for (i in badgesPoints.indices) {
            if (data.totalPoints > badgesPoints[i] && badge == badges[i]) {
                if (i < badgesPoints.size - 1) {
                    return i
                    break
                }
            }
        }
        return -1
    }
}