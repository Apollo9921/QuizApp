package com.example.quizapp.presentation.utils

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln
import kotlin.math.pow

val small = 600.dp
val normal = 840.dp

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