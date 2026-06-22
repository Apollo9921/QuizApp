package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.util.PlayerLevel

class FetchBadgeUseCase {
    operator fun invoke(data: User): Int {
        return  PlayerLevel.getLevelByPoints(data.totalPoints).maxPoints
    }
}