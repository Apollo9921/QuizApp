package com.example.quizapp.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun widthOfScreen() =
    LocalContext.current.resources.displayMetrics.widthPixels.dp / LocalDensity.current.density

@Composable
fun textSizeByScreen(baseSize: TextUnit): TextUnit {
    val screenWidth = widthOfScreen()
    val smallWidth = 600.dp
    val mediumWidth = 840.dp

    return when {
        screenWidth < smallWidth -> baseSize
        screenWidth < mediumWidth -> (baseSize * 2)
        else -> (baseSize * 2.5)
    }
}

@Composable
fun componentSizeByScreen(baseSize: Dp): Dp {
    val screenWidth = widthOfScreen()
    val smallWidth = 600.dp
    val mediumWidth = 840.dp

    return when {
        screenWidth < smallWidth -> baseSize
        screenWidth < mediumWidth -> (baseSize * 1.8f)
        else -> (baseSize * 2.2f)
    }
}