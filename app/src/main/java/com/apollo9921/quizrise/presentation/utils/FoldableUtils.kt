package com.apollo9921.quizrise.presentation.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import android.app.Activity

enum class DevicePosture {
    Normal,
    Book,
    Tabletop
}

data class WindowInfo(
    val widthSizeClass: WindowWidthSizeClass,
    val posture: DevicePosture,
    val foldingFeature: FoldingFeature?
)

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowInfo(): WindowInfo {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val layoutInfo by WindowInfoTracker.getOrCreate(context)
        .windowLayoutInfo(context)
        .collectAsState(initial = WindowLayoutInfo(emptyList()))

    val foldingFeature = layoutInfo.displayFeatures
        .filterIsInstance<FoldingFeature>()
        .firstOrNull()

    val posture = when {
        foldingFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldingFeature.orientation == FoldingFeature.Orientation.VERTICAL -> DevicePosture.Book
        foldingFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldingFeature.orientation == FoldingFeature.Orientation.HORIZONTAL -> DevicePosture.Tabletop
        else -> DevicePosture.Normal
    }

    return WindowInfo(
        widthSizeClass = windowSizeClass.widthSizeClass,
        posture = posture,
        foldingFeature = foldingFeature
    )
}