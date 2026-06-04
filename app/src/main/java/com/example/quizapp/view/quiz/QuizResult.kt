package com.example.quizapp.view.quiz

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.quizapp.view.custom.*
import com.example.quizapp.presentation.userManager
import com.example.quizapp.presentation.userName
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import com.example.quizapp.viewModel.ResultsViewModel
import com.example.quizapp.viewModel.UserViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context
private lateinit var resultsViewModel: ResultsViewModel

@Composable
fun QuizResult(
    navHostController: NavHostController,
    category: String,
    correctAnswers: Int,
    incorrectAnswers: Int,
    viewModel: UserViewModel = koinViewModel<UserViewModel>()
) {
    context = LocalContext.current
    runBlocking { userName = userManager.userName.first().toString() }
    resultsViewModel = ResultsViewModel(context)

    var total = correctAnswers + incorrectAnswers
    val pointsReceived = correctAnswers * 5
    val pointsPossible = total * 5
    updateResults(category, correctAnswers, incorrectAnswers)
    updateUser(userName, pointsReceived, pointsPossible, viewModel)

    Scaffold(
        topBar = { TopBar() }
    ) { it ->
        BackHandler(enabled = true) {}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$correctAnswers/$total",
                color = White,
                fontSize =
                    if (mediaQueryWidth() <= small) {
                        40.sp
                    } else if (mediaQueryWidth() <= normal) {
                        45.sp
                    } else {
                        50.sp
                    },
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = stringResource(id = R.string.pointsReceived, pointsReceived),
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
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                onClick = {
                    navHostController.navigate(Destination.Categories.route)
                },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(width = 2.dp, color = White),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Purple40,
                    disabledContentColor = Purple40,
                    disabledContainerColor = Purple40
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.finish),
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
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
            .background(PurpleGrey40)
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
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int) {
    GlobalScope.launch(Dispatchers.IO) {
        resultsViewModel.updateCategory(
            category = category,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun updateUser(
    userName: String,
    pointsReceived: Int,
    pointsPossible: Int,
    viewModel: UserViewModel
) {
    GlobalScope.launch(Dispatchers.IO) {
        viewModel.updateUser(
            totalPoints = pointsReceived,
            totalPointsPossible = pointsPossible,
            name = userName
        )
    }
}