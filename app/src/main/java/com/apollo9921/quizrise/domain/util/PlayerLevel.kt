package com.apollo9921.quizrise.domain.util

import com.apollo9921.quizrise.R

enum class PlayerLevel(val badgeName: String, val minPoints: Int, val maxPoints: Int, val badgeSymbol: Int) {
    RECRUIT("Recruit", 0, 145, R.drawable.newbie),
    EXPLORER("Explorer", 150, 495, R.drawable.newbie),
    VETERAN("Veteran", 500, 1495, R.drawable.intermediate),
    STRATEGIST("Strategist", 1500, 4995, R.drawable.intermediate),
    ELITE_BRAIN("Elite Brain", 5000, 14995, R.drawable.adavanced),
    MENTALIST("Mentalist", 15000, 49995, R.drawable.adavanced),
    ORACLE("Oracle", 50000, 149995, R.drawable.legend),
    QUIZ_DEITY("Quiz Deity", 150000, Int.MAX_VALUE, R.drawable.legend);

    companion object {
        fun getLevelByPoints(points: Int): PlayerLevel {
            return entries.firstOrNull { points in it.minPoints..it.maxPoints } ?: RECRUIT
        }
    }
}