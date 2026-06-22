package com.apollo9921.quizrise.presentation.core

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.apollo9921.quizrise.presentation.utils.textSizeByScreen

@Composable
fun getTypography(): Typography {
    return Typography(
        titleLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = textSizeByScreen(22.sp),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = textSizeByScreen(18.sp),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = textSizeByScreen(16.sp),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        ),
        displaySmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = textSizeByScreen(13.sp),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )
    )
}