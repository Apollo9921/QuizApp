package com.apollo9921.quizrise.presentation.screens.results

import android.content.Context
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.presentation.components.BottomNavigationBar
import com.apollo9921.quizrise.presentation.components.ErrorScreen
import com.apollo9921.quizrise.presentation.components.Loading
import com.apollo9921.quizrise.presentation.core.Black
import com.apollo9921.quizrise.presentation.core.Blue
import com.apollo9921.quizrise.presentation.core.DarkGreen
import com.apollo9921.quizrise.presentation.core.Green
import com.apollo9921.quizrise.presentation.core.Orange
import com.apollo9921.quizrise.presentation.core.Pink40
import com.apollo9921.quizrise.presentation.core.Purple40
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.Red
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.core.Yellow
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.presentation.utils.formatTotalCount
import com.apollo9921.quizrise.presentation.utils.widthOfScreen
import org.koin.androidx.compose.koinViewModel
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.CategoryStat
import com.apollo9921.quizrise.presentation.components.OffscreenShareCardHost
import com.apollo9921.quizrise.presentation.components.QuizTooltipIcon
import com.apollo9921.quizrise.presentation.components.ShareCardData
import com.apollo9921.quizrise.presentation.utils.shareQuizBitmap
import kotlinx.coroutines.launch

@Composable
fun ResultsRoute(
    navHostController: NavHostController,
    viewModel: ResultsViewModel = koinViewModel<ResultsViewModel>()
) {
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val retry = { viewModel.fetchUserAndResults() }

    LaunchedEffect(Unit) {
        viewModel.fetchUserAndResults()
    }

    Results(
        navHostController = navHostController,
        context = context,
        state = state,
        retry = retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Results(
    navHostController: NavHostController,
    context: Context,
    state: ResultsViewModel.UIState,
    retry: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var graphicsLayer by remember { mutableStateOf<GraphicsLayer?>(null) }
    var isExporting by remember { mutableStateOf(false) }

    val shareText = stringResource(id = R.string.share_text)
    val titleIntent = stringResource(id = R.string.title_intent)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state is ResultsViewModel.UIState.Success) {
                    IconButton(
                        onClick = {
                            val layer = graphicsLayer ?: return@IconButton
                            isExporting = true

                            scope.launch {
                                try {
                                    val bitmap = layer.toImageBitmap().asAndroidBitmap()
                                    shareQuizBitmap(context, bitmap, shareText, titleIntent)
                                } finally {
                                    isExporting = false
                                }
                            }
                        },
                        enabled = !isExporting && graphicsLayer != null,
                        content = {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = White
                            )
                        }
                    )
                    QuizTooltipIcon(
                        text = stringResource(id = R.string.tooltip_results),
                        position = Arrangement.End
                    )
                }
            }
        },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .safeDrawingPadding()
                .padding(bottom = paddingValues.calculateBottomPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            when (state) {
                is ResultsViewModel.UIState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = state.message), onClick = retry)
                }

                ResultsViewModel.UIState.Loading -> {
                    Loading()
                }

                is ResultsViewModel.UIState.Success -> {
                    val shareCardData = remember(state) {
                        ShareCardData(
                            totalPoints = state.user.totalPoints,
                            overallAccuracy = state.averagePercentage,
                            categories = state.results.mapIndexed { index, it ->
                                val percentage = if (it.correctAnswers == 0 && it.incorrectAnswers == 0) 0 else (it.correctAnswers * 100) / (it.correctAnswers + it.incorrectAnswers)
                                CategoryStat(
                                    name = state.data.keys.elementAt(index),
                                    percentage = percentage
                                )
                            }
                        )
                    }

                    OffscreenShareCardHost(
                        data = shareCardData,
                        onGraphicsLayerReady = { layer ->
                            graphicsLayer = layer
                        }
                    )

                    ShowResults(state.user, state.data)
                }
            }
        }
    }
}

@Composable
private fun ShowResults(user: User, data: Map<Int, Int>) {
    val screenWidth = widthOfScreen()

    val maxLayoutWidth =
        if (screenWidth < 600.dp) Dp.Unspecified else componentSizeByScreen(baseSize = 840.dp)
    val columnsCount = when {
        screenWidth < 600.dp -> 2
        screenWidth < 840.dp -> 3
        else -> 4
    }

    val chartSize = componentSizeByScreen(baseSize = 180.dp)
    val chartStroke = componentSizeByScreen(baseSize = 16.dp)
    val colors = listOf(Purple40, Pink40, White, Yellow, Red, DarkGreen, Green, Blue, Orange, Black)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .widthIn(max = maxLayoutWidth)
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item(span = { GridItemSpan(columnsCount) }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    style = MaterialTheme.typography.labelSmall,
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
            }
        }

        items(data.size) { index ->
            DetailsPieChartItem(
                categoryName = stringResource(data.keys.elementAt(index)),
                percentage = data.values.elementAt(index),
                color = colors[index % colors.size]
            )
        }
    }
}

@Composable
private fun PieChart(
    data: Map<Int, Int>,
    chartSize: Dp,
    chartBarWidth: Dp,
    animDuration: Int = 1000
) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    val averagePercentage = if (data.isNotEmpty()) data.values.average().toInt() else 0

    data.values.forEachIndexed { index, values ->
        val share = if (totalSum == 0) 0f else 360 * values.toFloat() / totalSum.toFloat()
        floatValue.add(index, share)
    }

    val colors = listOf(Purple40, Pink40, White, Yellow, Red, DarkGreen, Green, Blue, Orange, Black)

    var animationPlayed by rememberSaveable { mutableStateOf(false) }
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
private fun DetailsPieChartItem(
    categoryName: String,
    percentage: Int,
    color: Color
) {
    val itemHeight = componentSizeByScreen(baseSize = 70.dp)
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
                    style = MaterialTheme.typography.displaySmall,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.labelSmall,
                    color = White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}