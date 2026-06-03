package com.example.quizapp.view.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.view.components.ErrorScreen
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White
import com.example.quizapp.viewModel.QuizViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StartQuizRoute(
    navHostController: NavHostController,
    category: String,
    level: String,
    viewModel: QuizViewModel = koinViewModel { parametersOf(category, level) }
) {
    val uiState = viewModel.uiState.collectAsState().value
    val quizState = viewModel.quizState.collectAsState().value
    val correctAnswer = remember {
        { currentPage: Int ->
            viewModel.incrementCorrectAnswer(
                currentPage,
                navHostController
            )
        }
    }
    val incorrectAnswer = remember {
        { currentPage: Int ->
            viewModel.incrementIncorrectAnswer(
                currentPage,
                navHostController
            )
        }
    }
    val retry = remember { { viewModel.getQuiz() } }
    val resetValues = remember { { viewModel.resetValues() } }
    StartQuiz(uiState, quizState, correctAnswer, incorrectAnswer, resetValues, retry)
}

@Composable
private fun StartQuiz(
    uiState: QuizViewModel.UIState,
    quizState: QuizViewModel.QuizState,
    correctAnswer: (Int) -> Unit,
    incorrectAnswer: (Int) -> Unit,
    resetValues: () -> Unit,
    retry: () -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            resetValues()
        }
    }

    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(20.dp)
    ) { pv ->
        when (uiState) {
            QuizViewModel.UIState.Loading -> {
                Loading()
            }

            is QuizViewModel.UIState.Error -> {
                ErrorScreen(uiState.errorMessage) { retry() }
            }

            is QuizViewModel.UIState.Success -> {
                ShowQuiz(
                    pv,
                    uiState.quiz,
                    correctAnswer,
                    incorrectAnswer,
                    quizState
                )
            }
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PurpleGrey40),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(id = R.string.quiz),
            color = White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowQuiz(
    it: PaddingValues,
    data: List<QuizDTO>,
    correctAnswer: (Int) -> Unit,
    incorrectAnswer: (Int) -> Unit,
    quizState: QuizViewModel.QuizState
) {
    val state = rememberPagerState(pageCount = { data.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(quizState.progress) {
        if (quizState.progress == 0) {
            incorrectAnswer(state.currentPage)
            state.animateScrollToPage(state.currentPage + 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(top = it.calculateTopPadding())
    ) {
        HorizontalPager(
            state = state,
            userScrollEnabled = false
        ) { pageNumber ->
            val currentQuestion = data[pageNumber]
            val options = remember(pageNumber) {
                val list = currentQuestion.incorrectAnswers.toMutableList()
                list.add(currentQuestion.correctAnswer)
                list.shuffled()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = currentQuestion.question.text,
                    color = White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.padding(10.dp))

                options.forEach { answer ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(width = 3.dp, color = White),
                        colors = CardDefaults.cardColors(containerColor = Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clickable {
                                if (answer == currentQuestion.correctAnswer) {
                                    correctAnswer(state.currentPage)
                                } else {
                                    incorrectAnswer(state.currentPage)
                                }
                                coroutineScope.launch {
                                    state.animateScrollToPage(state.currentPage + 1)
                                }
                            }
                    ) {
                        Text(
                            text = answer,
                            color = White,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${quizState.progress}",
                    color = White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Question ${pageNumber + 1} / ${data.size}",
                    color = White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}