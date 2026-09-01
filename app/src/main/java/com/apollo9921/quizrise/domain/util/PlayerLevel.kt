package com.apollo9921.quizrise.domain.util

import com.apollo9921.quizrise.R

enum class PlayerLevel(
    val badgeName: String,
    val minPoints: Int,
    val maxPoints: Int,
    val badgeSymbol: Int,
    val resourceId: Int
) {
    RECRUIT("Recruit", 0, 145, R.drawable.newbie, R.string.recruit_level),
    EXPLORER("Explorer", 150, 495, R.drawable.newbie, R.string.explorer_level),
    VETERAN("Veteran", 500, 1495, R.drawable.intermediate, R.string.veteran_level),
    STRATEGIST("Strategist", 1500, 4995, R.drawable.intermediate, R.string.strategist_level),
    ELITE_BRAIN("Elite Brain", 5000, 14995, R.drawable.adavanced, R.string.elite_brain_level),
    MENTALIST("Mentalist", 15000, 49995, R.drawable.adavanced, R.string.mentalist_level),
    ORACLE("Oracle", 50000, 149995, R.drawable.legend, R.string.oracle_level),
    QUIZ_DEITY("Quiz Deity", 150000, Int.MAX_VALUE, R.drawable.legend, R.string.quiz_deity_level);

    companion object {
        fun getLevelByPoints(points: Int): PlayerLevel {
            return entries.firstOrNull { points in it.minPoints..it.maxPoints } ?: RECRUIT
        }

        fun getAllLevels(): List<PlayerLevel> {
            return entries.toList()
        }

        fun getLevelByName(resourceId: Int): String {
            return entries.firstOrNull { it.resourceId == resourceId }?.badgeName ?: RECRUIT.badgeName
        }

        fun getResourceByName(name: String): Int {
            return entries.firstOrNull { it.badgeName == name }?.resourceId ?: RECRUIT.resourceId
        }
    }
}