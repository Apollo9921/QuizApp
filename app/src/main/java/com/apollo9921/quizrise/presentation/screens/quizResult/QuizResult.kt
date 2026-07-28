package com.apollo9921.quizrise.presentation.screens.quizResult

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.core.Black
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.widthOfScreen
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.apollo9921.quizrise.R

@Composable
fun QuizResultRoute(
    navHostController: NavHostController,
    category: String,
    correctAnswers: Int,
    incorrectAnswers: Int,
    question: Array<String>,
    answers: Array<String>,
    correctAnswersList: Array<String>,
    viewModel: QuizResultViewModel = koinViewModel {
        parametersOf(
            category,
            correctAnswers,
            incorrectAnswers
        )
    }
) {
    val total = viewModel.total
    val pointsReceived = viewModel.pointsReceived.intValue
    val navigateToWrongAnswers = {
        navHostController.navigate(
            Destination.WrongAnswers.passArgument(
                question = question,
                correctAnswers = correctAnswersList,
                incorrectAnswers = answers
            )
        )
    }

    QuizResultScreen(
        navHostController = navHostController,
        correctAnswers = correctAnswers,
        total = total,
        pointsReceived = pointsReceived,
        navigateToWrongAnswers = navigateToWrongAnswers
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizResultScreen(
    navHostController: NavHostController,
    correctAnswers: Int,
    total: Int,
    pointsReceived: Int,
    navigateToWrongAnswers: () -> Unit,
) {
    BackHandler(enabled = true) {}

    val screenWidth = widthOfScreen()
    val maxLayoutWidth = if (screenWidth < 600.dp) Dp.Unspecified else componentSizeByScreen(480.dp)

    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = PurpleGrey40,
                isBackEnabled = false,
                onBackClicked = { false },
                title = stringResource(id = R.string.results)
            )
        },
        containerColor = PurpleGrey40
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxLayoutWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, White.copy(alpha = 0.15f)),
                    colors = CardDefaults.cardColors(
                        containerColor = White.copy(alpha = 0.06f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.correct_answers),
                            style = MaterialTheme.typography.labelSmall,
                            color = White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "$correctAnswers / $total",
                            style = MaterialTheme.typography.titleLarge,
                            color = White,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(
                            color = White.copy(alpha = 0.1f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(
                                id = R.string.pointsReceived,
                                pointsReceived
                            ).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = White,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 40.dp)))

                if (correctAnswers < total) {
                    Text(
                        text = stringResource(id = R.string.see_wrong_answers),
                        style = MaterialTheme.typography.labelMedium,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable { navigateToWrongAnswers() }
                    )
                    Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 40.dp)))
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(componentSizeByScreen(baseSize = 54.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            navHostController.navigate(Destination.Categories.route) {
                                popUpTo(Destination.Categories.route) { inclusive = true }
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.finish),
                            color = Black,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}