package com.apollo9921.quizrise.presentation.screens.quiz

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.data.network.dto.TranslatedQuizResult
import com.apollo9921.quizrise.presentation.components.ErrorScreen
import com.apollo9921.quizrise.presentation.components.Loading
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.presentation.utils.widthOfScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.GuestLimitBottomSheet

@Composable
fun StartQuizRoute(
    navHostController: NavHostController,
    category: String,
    level: String,
    viewModel: QuizViewModel = koinViewModel { parametersOf(category, level) }
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val quizState = viewModel.quizState.collectAsStateWithLifecycle().value

    val correctAnswer = remember {
        { currentPage: Int ->
            viewModel.incrementCorrectAnswer(currentPage, navHostController)
        }
    }
    val incorrectAnswer = remember {
        { currentPage: Int ->
            viewModel.incrementIncorrectAnswer(currentPage, navHostController)
        }
    }
    val retry = remember { { viewModel.getQuiz() } }
    val resetValues = remember { { viewModel.resetValues() } }
    val goBack = remember { { navHostController.navigateUp() } }
    val signInByGoogle = remember { { viewModel.startSignInByGoogle(navHostController) } }

    StartQuiz(
        uiState,
        quizState,
        correctAnswer,
        incorrectAnswer,
        resetValues,
        retry,
        goBack,
        signInByGoogle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartQuiz(
    uiState: QuizViewModel.UIState,
    quizState: QuizViewModel.QuizState,
    correctAnswer: (Int) -> Unit,
    incorrectAnswer: (Int) -> Unit,
    resetValues: () -> Unit,
    retry: () -> Unit,
    goBack: () -> Boolean,
    signInByGoogle: () -> Unit
) {
    Scaffold { pv ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(pv)
        ) {
            when (uiState) {
                QuizViewModel.UIState.Loading -> {
                    Loading(message = stringResource(R.string.loading_translations))
                }

                is QuizViewModel.UIState.Error -> {
                    if (uiState.errorMessage == R.string.anonymous_quiz_expired || uiState.showToast) {
                        GuestLimitBottomSheet(
                            isVisible = true,
                            onDismissRequest = { goBack() },
                            onGoogleSignInClick = { signInByGoogle() },
                            onEmailRegisterClick = { }
                        )
                        if (uiState.showToast) {
                            Toast.makeText(
                                LocalContext.current,
                                stringResource(uiState.errorMessage),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        ErrorScreen(
                            errorMessage = stringResource(uiState.errorMessage),
                            onClick = { retry() }
                        )
                    }
                }

                is QuizViewModel.UIState.Success -> {
                    ShowQuiz(
                        data = uiState.quiz,
                        correctAnswer = correctAnswer,
                        incorrectAnswer = incorrectAnswer,
                        quizState = quizState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowQuiz(
    data: List<TranslatedQuizResult>,
    correctAnswer: (Int) -> Unit,
    incorrectAnswer: (Int) -> Unit,
    quizState: QuizViewModel.QuizState
) {
    val state = rememberPagerState(pageCount = { data.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(quizState.progress) {
        if (quizState.progress == 0) {
            val currentPage = state.currentPage
            if (currentPage < data.size - 1) {
                state.animateScrollToPage(currentPage + 1)
            }
            incorrectAnswer(currentPage)
        }
    }

    val screenWidth = widthOfScreen()
    val maxLayoutWidth = if (screenWidth < 600.dp) Dp.Unspecified else componentSizeByScreen(560.dp)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        HorizontalPager(
            state = state,
            userScrollEnabled = false,
            modifier = Modifier
                .widthIn(max = maxLayoutWidth)
                .fillMaxSize()
        ) { pageNumber ->
            val currentQuestion = data[pageNumber]
            val options = remember(currentQuestion.question) {
                val list = currentQuestion.incorrectAnswers.toMutableList()
                list.add(currentQuestion.correctAnswer)
                list.shuffled()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = currentQuestion.question,
                        color = White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 24.dp)))

                options.forEachIndexed { index, answer ->
                    val optionLetter = when (index) {
                        0 -> "A"
                        1 -> "B"
                        2 -> "C"
                        3 -> "D"
                        else -> ""
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(width = 1.dp, color = White.copy(alpha = 0.15f)),
                        colors = CardDefaults.cardColors(
                            containerColor = White.copy(alpha = 0.06f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                val currentPage = state.currentPage
                                coroutineScope.launch {
                                    if (currentPage < data.size - 1) {
                                        state.animateScrollToPage(currentPage + 1)
                                    }
                                    if (answer == currentQuestion.correctAnswer) {
                                        correctAnswer(currentPage)
                                    } else {
                                        incorrectAnswer(currentPage)
                                    }
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(componentSizeByScreen(baseSize = 36.dp))
                                    .background(
                                        color = White.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    color = White.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = answer,
                                color = White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 40.dp)))

                Text(
                    text = "${quizState.progress}",
                    color = White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Question ${pageNumber + 1} / ${data.size}",
                    color = White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}