package com.apollo9921.quizrise.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun widthOfScreen(): Dp {
    val configuration = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current

    return with(density) { configuration.width.toDp() }
}

@Composable
fun heightOfScreen(): Dp {
    val configuration = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current

    return with(density) { configuration.height.toDp() }
}

@Composable
fun textSizeByScreen(baseSize: TextUnit): TextUnit {
    val screenWidth = widthOfScreen()
    val screenHeight = heightOfScreen()

    val smallWidth = 600.dp
    val mediumWidth = 840.dp

    val smallHeight = 480.dp
    val mediumHeight = 900.dp

    return when {
        screenWidth < smallWidth || screenHeight < smallHeight -> baseSize

        screenWidth < mediumWidth && screenHeight < mediumHeight -> (baseSize * 1.5)

        else -> (baseSize * 2.0)
    }
}

@Composable
fun componentSizeByScreen(baseSize: Dp): Dp {
    val screenWidth = widthOfScreen()
    val screenHeight = heightOfScreen()

    return when {
        screenWidth < 600.dp || screenHeight < 480.dp -> baseSize
        screenWidth < 840.dp && screenHeight < 900.dp -> (baseSize * 1.5f)
        else -> (baseSize * 1.8f)
    }
}