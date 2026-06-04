package com.example.quizapp.view.custom

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.quizapp.R
import kotlin.math.ln
import kotlin.math.pow

val small = 600.dp
val normal = 840.dp

val onBoardingTitle = listOf(
    R.string.onBoard1,
    R.string.onBoard2,
    R.string.onBoard3,
    R.string.onBoard4,
    R.string.onBoard5,
)

val onBoardingAnimation = listOf(
    R.raw.welcome,
    R.raw.questions,
    R.raw.results,
    R.raw.badges,
    R.raw.start,
)

val badges = listOf(
    R.drawable.newbie,
    R.drawable.intermediate,
    R.drawable.adavanced,
    R.drawable.legend
)

val badgesDescription = listOf(
    R.string.newbie,
    R.string.intermediate,
    R.string.advanced,
    R.string.legend,
)

val badgesPoints = listOf(
    1000,
    10000,
    100000,
    1000000
)

val categories = listOf(
    R.string.artsAndLiterature,
    R.string.filmAndTV,
    R.string.foodAndDrink,
    R.string.generalKnowledge,
    R.string.geography,
    R.string.history,
    R.string.music,
    R.string.science,
    R.string.societyAndCulture,
    R.string.sportAndLeisure
)

@Composable
fun mediaQueryWidth(): Dp {
    return LocalContext.current.resources.displayMetrics.widthPixels.dp / LocalDensity.current.density
}

fun formatTotalCount(count: Float): String {
    if (count < 1000) return count.toInt().toString()
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        count / 1000.0.pow(exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}