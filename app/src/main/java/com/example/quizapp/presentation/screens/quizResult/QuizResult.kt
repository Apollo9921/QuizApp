package com.example.quizapp.presentation.screens.quizResult

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QuizResultRoute(
    navHostController: NavHostController,
    category: String,
    correctAnswers: Int,
    incorrectAnswers: Int,
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

    QuizResultScreen(
        navHostController = navHostController,
        correctAnswers = correctAnswers,
        total = total,
        pointsReceived = pointsReceived,
    )
}

@Composable
private fun QuizResultScreen(
    navHostController: NavHostController,
    correctAnswers: Int,
    total: Int,
    pointsReceived: Int,
) {
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
                style = MaterialTheme.typography.labelMedium,
                text = "$correctAnswers/$total",
                color = White
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(id = R.string.pointsReceived, pointsReceived),
                color = White
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
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(id = R.string.finish),
                    color = White,
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
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(id = R.string.results),
            color = White
        )
    }
}