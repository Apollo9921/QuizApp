package com.apollo9921.quizrise.presentation.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PurpleGrey40,
    secondary = Purple40,
    tertiary = Pink40,
    surface = White,
    onError = Red
)

@Composable
fun QuizAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = content
    )
}