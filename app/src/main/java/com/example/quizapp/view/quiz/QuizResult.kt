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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.model.database.QuizDatabase
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.main.userManager
import com.example.quizapp.view.main.userName
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.Purple40
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White
import com.example.quizapp.viewModel.ResultsViewModel
import com.example.quizapp.viewModel.UserViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first


@SuppressLint("StaticFieldLeak")
private lateinit var context: Context
private lateinit var owner: LifecycleOwner
private lateinit var userViewModel: UserViewModel
private lateinit var resultsViewModel: ResultsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResult(
    navHostController: NavHostController,
    category: String,
    correctAnswers: Int,
    incorrectAnswers: Int
) {
    owner = LocalLifecycleOwner.current
    context = LocalContext.current
    runBlocking { userName = userManager.userName.first().toString() }
    userViewModel = UserViewModel(context)
    resultsViewModel = ResultsViewModel(context)

    val total = correctAnswers + incorrectAnswers
    val pointsReceived = correctAnswers * 5
    val pointsPossible = total * 5
    if (isLoadedUser.value && isLoadedResults.value && count == 0) {
        updateResults(category, correctAnswers, incorrectAnswers)
        updateUser(userName, pointsReceived, pointsPossible)
        count++
    }

    Scaffold(
        topBar = { TopBar() }
    ) { it ->
        BackHandler(enabled = true) {}
        if (isLoadedUser.value && isLoadedResults.value) {
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
                        navHostController.popBackStack(Destination.Main.route, inclusive = true)
                        navHostController.navigate(Destination.Main.route)
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
        } else {
            Loading()
            if (totalPoints == 0 && totalPointsPossible == 0) {
                QuizDatabase
                    .getDatabase(context)
                    .userDao()
                    .getUserProfile()
                    .observe(owner) {
                        totalPoints = it.totalPoints
                        totalPointsPossible = it.totalPointsPossible
                        isLoadedUser.value = true
                    }
            }
            if (correctAnswersBefore == 0 && incorrectAnswersBefore == 0) {
                QuizDatabase
                    .getDatabase(context)
                    .resultsDao()
                    .getSpecificCategory(category)
                    .observe(owner) {
                        correctAnswersBefore = it.correctAnswers
                        incorrectAnswersBefore = it.incorrectAnswers
                        isLoadedResults.value = true
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
    correctAnswersBefore += correctAnswers
    incorrectAnswersBefore += incorrectAnswers
    GlobalScope.launch(Dispatchers.IO) {
        resultsViewModel.updateCategory(
            category = category,
            correctAnswers = correctAnswersBefore,
            incorrectAnswers = incorrectAnswersBefore
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun updateUser(userName: String, pointsReceived: Int, pointsPossible: Int) {
    totalPoints += pointsReceived
    totalPointsPossible += pointsPossible
    GlobalScope.launch(Dispatchers.IO) {
        userViewModel.updateUser(
            totalPoints = totalPoints,
            totalPointsPossible = totalPointsPossible,
            name = userName
        )
    }
}