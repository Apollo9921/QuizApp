package com.example.quizapp.view

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.local.entity.ResultsEntity
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.view.custom.*
import com.example.quizapp.presentation.core.Black
import com.example.quizapp.presentation.core.Blue
import com.example.quizapp.presentation.core.DarkGreen
import com.example.quizapp.presentation.core.Green
import com.example.quizapp.presentation.core.Orange
import com.example.quizapp.presentation.core.Pink40
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.Red
import com.example.quizapp.presentation.core.White
import com.example.quizapp.presentation.core.Yellow

private lateinit var user: SnapshotStateList<UserEntity>
private lateinit var results: SnapshotStateList<ResultsEntity>
private lateinit var data: Map<String, Int>

@Composable
fun Results(navHostController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(bottom = it.calculateBottomPadding())
        ) {
            user = remember { mutableStateListOf() }
            results = remember { mutableStateListOf() }
            QuizDatabase.getDatabase(LocalContext.current)
                .userDao().fetchUserProfile()
                .observe(LocalLifecycleOwner.current) {
                    if (user.isNotEmpty()) {
                        user.clear()
                    }
                    user.add(it)
                }
            QuizDatabase.getDatabase(LocalContext.current)
                .resultsDao().getResults()
                .observe(LocalLifecycleOwner.current) {
                    if (results.isNotEmpty()) {
                        results.clear()
                    }
                    for (i in it.indices) {
                        results.add(it[i])
                    }
                }

            if (user.isNotEmpty() && results.isNotEmpty()) {
                ShowResults()
            }
        }
    }
}

@Composable
private fun ShowResults() {
    for (i in results.indices) {
        val correct = results[i].correctAnswers
        val incorrect = results[i].incorrectAnswers
        if (i == 0) {
            data = if (correct == 0 && incorrect == 0) {
                mapOf(stringResource(id = categories[i]) to 0)
            } else if (incorrect == 0 && correct > 0) {
                mapOf(stringResource(id = categories[i]) to 100)
            } else if (correct == 0 && incorrect > 0) {
                mapOf(stringResource(id = categories[i]) to 0)
            } else {
                val percentage = (correct * 100) / (correct + incorrect)
                mapOf(stringResource(id = categories[i]) to percentage)
            }
        } else {
            data = if (correct == 0 && incorrect == 0) {
                data + mapOf(stringResource(id = categories[i]) to 0)
            } else if (incorrect == 0 && correct > 0) {
                data + mapOf(stringResource(id = categories[i]) to 100)
            } else if (correct == 0 && incorrect > 0) {
                data + mapOf(stringResource(id = categories[i]) to 0)
            } else {
                val percentage = (correct * 100) / (correct + incorrect)
                data + mapOf(stringResource(id = categories[i]) to percentage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.results),
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(
                    id = R.string.totalAndTotalPossiblePoints,
                    formatTotalCount(user[0].totalPoints.toFloat()),
                    formatTotalCount(user[0].totalPointsPossible.toFloat())
                ),
                color = White,
                fontSize =
                    if (mediaQueryWidth() <= small) {
                        25.sp
                    } else if (mediaQueryWidth() <= normal) {
                        30.sp
                    } else {
                        35.sp
                    },
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        PieChart(
            data = data
        )
    }
}

@Composable
private fun PieChart(
    data: Map<String, Int>,
    radiusOuter: Dp = if (mediaQueryWidth() <= small) {
        70.dp
    } else if (mediaQueryWidth() <= normal) {
        100.dp
    } else {
        130.dp
    },
    chartBarWidth: Dp = if (mediaQueryWidth() <= small) {
        15.dp
    } else if (mediaQueryWidth() <= normal) {
        25.dp
    } else {
        35.dp
    },
    animDuration: Int = 1000,
) {

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    val colors = listOf(
        Purple40,
        Pink40,
        White,
        Black,
        Red,
        Yellow,
        DarkGreen,
        Green,
        Blue,
        Orange
    )

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }
    }
    DetailsPieChart(
        data = data,
        colors = colors
    )
}

@Composable
fun DetailsPieChart(
    data: Map<String, Int>,
    colors: List<Color>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        items(data.size) { item ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(item), data.values.elementAt(item)),
                color = colors[item]
            )
        }
    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Int>,
    height: Dp = if (mediaQueryWidth() <= small) {
        45.dp
    } else if (mediaQueryWidth() <= normal) {
        55.dp
    } else {
        65.dp
    },
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(10.dp)
                )
                .size(height)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 15.dp),
                text = data.first,
                color = Black,
                textAlign = TextAlign.Start
            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 15.dp),
                text = data.second.toString() + "%",
                color = White
            )
        }
    }
}