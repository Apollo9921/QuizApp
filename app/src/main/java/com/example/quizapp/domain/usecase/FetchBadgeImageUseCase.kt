package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.util.PlayerLevel

class FetchBadgeImageUseCase {
    operator fun invoke(data: User): Int {
        return PlayerLevel.getLevelByPoints(data.totalPoints).badgeSymbol
    }
}