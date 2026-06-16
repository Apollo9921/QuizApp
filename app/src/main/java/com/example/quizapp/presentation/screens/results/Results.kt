package com.example.quizapp.presentation.screens.results

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.components.Loading
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
import com.example.quizapp.presentation.utils.formatTotalCount
import org.koin.androidx.compose.koinViewModel

private val categories = listOf(
    R.string.artsAndLiterature_translatable,
    R.string.filmAndTV_translatable,
    R.string.foodAndDrink_translatable,
    R.string.generalKnowledge_translatable,
    R.string.geography_translatable,
    R.string.history_translatable,
    R.string.music_translatable,
    R.string.science_translatable,
    R.string.societyAndCulture_translatable,
    R.string.sportAndLeisure_translatable
)

@Composable
fun ResultsRoute(
    navHostController: NavHostController,
    viewModel: ResultsViewModel = koinViewModel<ResultsViewModel>()
) {
    val state = viewModel.uiState.collectAsState().value
    val retry = { viewModel.fetchUserAndResults() }
    LaunchedEffect(Unit) {
        viewModel.fetchUserAndResults()
    }

    Results(
        navHostController = navHostController,
        state = state,
        retry = retry
    )
}

@Composable
private fun Results(
    navHostController: NavHostController,
    state: ResultsViewModel.UIState,
    retry: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .safeDrawingPadding()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            when(state) {
                is ResultsViewModel.UIState.Error -> {
                    ErrorScreen(
                        errorMessage = stringResource(id = state.message),
                        onClick = retry
                    )
                }
                ResultsViewModel.UIState.Loading -> {
                    Loading()
                }
                is ResultsViewModel.UIState.Success -> {
                    ShowResults(state.user, state.results)
                }
            }
        }
    }
}

@Composable
private fun ShowResults(user: User, results: List<Results>) {
    var data: Map<String, Int> = mapOf()
    val displayCount = minOf(results.size, categories.size)
    for (i in 0 until displayCount) {
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
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(id = R.string.results),
                color = White
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(
                    id = R.string.totalAndTotalPossiblePoints,
                    formatTotalCount(user.totalPoints.toFloat()),
                    formatTotalCount(user.totalPointsPossible.toFloat())
                ),
                color = White
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
    radiusOuter: Dp = 80.dp,
    chartBarWidth: Dp = 20.dp,
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
                color = colors[item % colors.size]
            )
        }
    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Int>,
    height: Dp = 50.dp,
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