package com.example.quizapp.view.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.model.quiz.QuizItem
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White
import com.example.quizapp.viewModel.QuizViewModel
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context
private lateinit var quiz: ArrayList<QuizItem>
private var loading = mutableStateOf(false)
private var success = mutableStateOf(false)
private var error = mutableStateOf(false)
private var cancel = mutableStateOf(false)
private var correctAnswers = mutableStateOf(0)
private var incorrectAnswers = mutableStateOf(0)
private var progress = mutableStateOf(20)
private var quizViewModel: QuizViewModel = QuizViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartQuiz(navHostController: NavHostController, category: String, level: String) {
    context = LocalContext.current
    correctAnswers.value = 0
    incorrectAnswers.value = 0
    getQuiz(category, level)
    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(20.dp)
    ) {
        if (success.value) {
            BackHandler(enabled = true) {}
            ShowQuiz(it, navHostController, category)
        }
    }
    GetResult(navHostController)
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun ShowQuiz(it: PaddingValues, navHostController: NavHostController, category: String) {
    val state = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(progress.value) {
        delay(1000)
        progress.value--
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = it.calculateTopPadding())
    ) {
        HorizontalPager(
            pageCount = quiz.size,
            state = state,
            userScrollEnabled = false
        ) { index ->
            val answers: ArrayList<String> = ArrayList()
            answers.add(quiz[index].correctAnswer)
            for (i in 0 until 3) {
                answers.add(quiz[index].incorrectAnswers[i])
            }
            answers.sort()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PurpleGrey40),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Text(
                        text = quiz[index].question,
                        color = White,
                        fontSize =
                        if (mediaQueryWidth() <= small) {
                            20.sp
                        } else if (mediaQueryWidth() <= normal) {
                            25.sp
                        } else {
                            30.sp
                        },
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    for (i in 0 until answers.size) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(width = 3.dp, color = White),
                            colors = CardDefaults.cardColors(
                                containerColor = Black,
                                contentColor = White,
                                disabledContentColor = White,
                                disabledContainerColor = Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp)
                                .clickable {
                                    if (answers[i] == quiz[index].correctAnswer) {
                                        correctAnswers.value++
                                    } else {
                                        incorrectAnswers.value++
                                    }
                                    progress.value = 20
                                    if (state.currentPage < quiz.size - 1) {
                                        coroutineScope.launch { state.animateScrollToPage(state.currentPage + 1) }
                                    } else {
                                        navHostController.navigate(
                                            Destination.QuizResult.passArgument(
                                                category = category,
                                                correctAnswers = correctAnswers.value,
                                                incorrectAnswers = incorrectAnswers.value
                                            )
                                        )
                                    }
                                }
                        ) {
                            Text(
                                text = answers[i],
                                color = White,
                                fontSize =
                                if (mediaQueryWidth() <= small) {
                                    30.sp
                                } else if (mediaQueryWidth() <= normal) {
                                    35.sp
                                } else {
                                    40.sp
                                },
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Text(
                        text = progress.value.toString(),
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
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                    )
                    if (progress.value == 0) {
                        incorrectAnswers.value++
                        progress.value = 20
                        if (state.currentPage < quiz.size - 1) {
                            coroutineScope.launch { state.animateScrollToPage(page = state.currentPage + 1) }
                        } else {
                            navHostController.navigate(
                                Destination.QuizResult.passArgument(
                                    category = category,
                                    correctAnswers = correctAnswers.value,
                                    incorrectAnswers = incorrectAnswers.value
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getQuiz(category: String, level: String) {
    loading.value = true
    success.value = false
    CoroutineScope(Dispatchers.IO).launch {
        quizViewModel.getQuiz(category, level, context)
        quizViewModel.getQuizUIState.collect {
            when (it) {
                QuizViewModel.QuizUIState.Cancel -> {
                    loading.value = false
                    cancel.value = true
                }
                QuizViewModel.QuizUIState.Error -> {
                    loading.value = false
                    error.value = true
                }
                is QuizViewModel.QuizUIState.Success -> {
                    quiz = Json.decodeFromString(it.quiz.body())
                    loading.value = false
                    success.value = true
                }
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
            text = stringResource(id = R.string.quiz),
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
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GetResult(navHostController: NavHostController) {
    when {
        loading.value -> {
            Loading()
        }
        error.value -> {
            navHostController.popBackStack(Destination.LevelDifficulty.route, inclusive = true)
            navHostController.navigate(Destination.LevelDifficulty.route)
            Toast.makeText(context, stringResource(id = R.string.error), Toast.LENGTH_SHORT).show()
        }
        cancel.value -> {
            navHostController.popBackStack(Destination.LevelDifficulty.route, inclusive = true)
            navHostController.navigate(Destination.LevelDifficulty.route)
            Toast.makeText(context, stringResource(id = R.string.noInternet), Toast.LENGTH_SHORT)
                .show()
        }
    }
}