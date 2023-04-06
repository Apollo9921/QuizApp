package com.example.quizapp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R
import com.example.quizapp.model.database.QuizDatabase
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.main.isSplashScreenOpen
import com.example.quizapp.view.theme.White

private lateinit var totalPoints: SnapshotStateList<Int>
private lateinit var badge: SnapshotStateList<Int>

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

@Composable
fun Progress() {
    context = LocalContext.current
    totalPoints = remember { mutableStateListOf() }
    badge = remember { mutableStateListOf() }
    QuizDatabase.getDatabase(context)
        .userDao().getUserProfile()
        .observe(LocalLifecycleOwner.current) {
            if (totalPoints.isNotEmpty() && badge.isNotEmpty()) {
                totalPoints.clear()
                badge.clear()
            }
            totalPoints.add(it.totalPoints)
            for (i in badgesPoints.indices) {
                if (it.totalPoints <= badgesPoints[i]) {
                    badge.add(badgesPoints[i])
                    break
                }
            }
        }
    if (totalPoints.isNotEmpty() && badge.isNotEmpty()) {
        isSplashScreenOpen = false
        var percentage = (totalPoints[0] * 100) / badge[0].toDouble()
        percentage *= 0.01
        val progress = remember { mutableStateOf(percentage.toFloat()) }
        val animatedProgress = animateFloatAsState(
            targetValue = progress.value,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.progress),
                color = White,
                fontSize =
                if (mediaQueryWidth() <= small) {
                    35.sp
                } else if (mediaQueryWidth() <= normal) {
                    40.sp
                } else {
                    45.sp
                },
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "${totalPoints[0]}/${badge[0]}",
                    color = White,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        25.sp
                    } else if (mediaQueryWidth() <= normal) {
                        35.sp
                    } else {
                        45.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(
                            y =
                            if (mediaQueryWidth() <= small) {
                                115.dp
                            } else if (mediaQueryWidth() <= normal) {
                                175.dp
                            } else {
                                235.dp
                            },
                            x =
                            if (mediaQueryWidth() <= small) {
                                70.dp
                            } else if (mediaQueryWidth() <= normal) {
                                130.dp
                            } else {
                                160.dp
                            }
                        )
                )
                CircularProgressIndicator(
                    progress = animatedProgress,
                    color = White,
                    strokeWidth =
                    if (mediaQueryWidth() <= small) {
                        15.dp
                    } else if (mediaQueryWidth() <= normal) {
                        20.dp
                    } else {
                        25.dp
                    },
                    modifier = Modifier
                        .size(
                            if (mediaQueryWidth() <= small) {
                                250.dp
                            } else if (mediaQueryWidth() <= normal) {
                                350.dp
                            } else {
                                450.dp
                            }
                        )
                )
            }
        }
    }
}