package com.example.quizapp.domain.util

import com.example.quizapp.R

enum class PlayerLevel(val badgeName: String, val minPoints: Int, val maxPoints: Int, val badgeSymbol: Int) {
    RECRUIT("Recruta", 0, 145, R.drawable.newbie),
    EXPLORER("Explorador", 150, 495, R.drawable.newbie),
    VETERAN("Veterano", 500, 1495, R.drawable.intermediate),
    STRATEGIST("Estratega", 1500, 4995, R.drawable.intermediate),
    ELITE_BRAIN("Cérebro de Elite", 5000, 14995, R.drawable.adavanced),
    MENTALIST("Mentalista", 15000, 49995, R.drawable.adavanced),
    ORACLE("Oráculo", 50000, 149995, R.drawable.legend),
    QUIZ_DEITY("Divindade do Quiz", 150000, Int.MAX_VALUE, R.drawable.legend);

    companion object {
        fun getLevelByPoints(points: Int): PlayerLevel {
            return entries.firstOrNull { points in it.minPoints..it.maxPoints } ?: RECRUIT
        }
    }
}