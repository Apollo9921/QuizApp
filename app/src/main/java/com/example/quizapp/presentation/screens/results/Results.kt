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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.quizapp.presentation.utils.componentSizeByScreen
import com.example.quizapp.presentation.utils.formatTotalCount
import com.example.quizapp.presentation.utils.widthOfScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Results(
    navHostController: NavHostController,
    state: ResultsViewModel.UIState,
    retry: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.results),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PurpleGrey40),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            when(state) {
                is ResultsViewModel.UIState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = state.message), onClick = retry)
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
        val percentage = if (correct == 0 && incorrect == 0) 0 else (correct * 100) / (correct + incorrect)
        data = data + mapOf(stringResource(id = categories[i]) to percentage)
    }

    val screenWidth = widthOfScreen()

    val maxLayoutWidth = if (screenWidth < 600.dp) Dp.Unspecified else 840.dp
    val columnsCount = when {
        screenWidth < 600.dp -> 2
        screenWidth < 840.dp -> 3
        else -> 4
    }

    val chartSize = componentSizeByScreen(baseSize = 180.dp)
    val chartStroke = componentSizeByScreen(baseSize = 16.dp)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = maxLayoutWidth)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(
                id = R.string.totalAndTotalPossiblePoints,
                formatTotalCount(user.totalPoints.toFloat()),
                formatTotalCount(user.totalPointsPossible.toFloat())
            ),
            color = White.copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PieChart(
            data = data,
            chartSize = chartSize,
            chartBarWidth = chartStroke
        )

        Spacer(modifier = Modifier.height(24.dp))

        DetailsPieChart(
            data = data,
            columnsCount = columnsCount
        )
    }
}

@Composable
private fun PieChart(
    data: Map<String, Int>,
    chartSize: Dp,
    chartBarWidth: Dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    val averagePercentage = if (data.isNotEmpty()) data.values.average().toInt() else 0

    data.values.forEachIndexed { index, values ->
        val share = if (totalSum == 0) 0f else 360 * values.toFloat() / totalSum.toFloat()
        floatValue.add(index, share)
    }

    val colors = listOf(Purple40, Pink40, White, Yellow, Red, DarkGreen, Green, Blue, Orange, Black)

    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) chartSize.value else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = LinearOutSlowInEasing),
        label = "size"
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 360f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = LinearOutSlowInEasing),
        label = "rotation"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(animateSize.dp)
                .rotate(animateRotation)
        ) {
            if (totalSum == 0) {
                drawArc(
                    color = White.copy(alpha = 0.1f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Round)
                )
            } else {
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = lastValue,
                        sweepAngle = value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Round)
                    )
                    lastValue += value
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$averagePercentage%",
                style = MaterialTheme.typography.labelMedium,
                color = White,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = stringResource(id = R.string.overall_average),
                style = MaterialTheme.typography.displaySmall,
                color = White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DetailsPieChart(
    data: Map<String, Int>,
    columnsCount: Int
) {
    val colors = listOf(Purple40, Pink40, White, Yellow, Red, DarkGreen, Green, Blue, Orange, Black)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(data.size) { index ->
            DetailsPieChartItem(
                categoryName = data.keys.elementAt(index),
                percentage = data.values.elementAt(index),
                color = colors[index % colors.size]
            )
        }
    }
}

@Composable
private fun DetailsPieChartItem(
    categoryName: String,
    percentage: Int,
    color: Color
) {
    val itemHeight = componentSizeByScreen(baseSize = 64.dp)
    val indicatorWidth = componentSizeByScreen(baseSize = 6.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.06f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(indicatorWidth)
                    .background(color, RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelSmall,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.displaySmall,
                    color = White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}