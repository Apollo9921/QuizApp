package com.example.quizapp.view.custom

import android.view.WindowManager
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.quizapp.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.quizapp.view.theme.*
import kotlin.math.ln
import kotlin.math.pow

val small = 600.dp
val normal = 840.dp
var internet = mutableStateOf(true)
var isLoadedUser = mutableStateOf(false)
var isLoadedResults = mutableStateOf(false)
var totalPoints = 0
var totalPointsPossible = 0
var correctAnswersBefore = 0
var incorrectAnswersBefore = 0
var count = 0

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

val categoriesImages = listOf(
    R.drawable.book,
    R.drawable.movie,
    R.drawable.food,
    R.drawable.knowledge,
    R.drawable.geography,
    R.drawable.history,
    R.drawable.music,
    R.drawable.science,
    R.drawable.society,
    R.drawable.sports
)

val levelsDifficulty = listOf(
    R.string.easy,
    R.string.medium,
    R.string.hard
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

@Composable
fun PieChart(
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
                modifier = Modifier.padding(start = 15.dp),
                text = data.first,
                fontWeight = FontWeight.Medium,
                fontSize =
                if (mediaQueryWidth() <= small) {
                    20.sp
                } else if (mediaQueryWidth() <= normal) {
                    30.sp
                } else {
                    40.sp
                },
                color = Black
            )
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = data.second.toString() + "%",
                fontWeight = FontWeight.Medium,
                fontSize =
                if (mediaQueryWidth() <= small) {
                    20.sp
                } else if (mediaQueryWidth() <= normal) {
                    30.sp
                } else {
                    40.sp
                },
                color = White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Loading() {
    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PurpleGrey40
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(
                    if (mediaQueryWidth() <= small) {
                        100.dp
                    } else if (mediaQueryWidth() <= normal) {
                        150.dp
                    } else {
                        200.dp
                    }
                )
            )
        }
    }
}