package com.apollo9921.quizrise.presentation.screens.wrongAnswers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.presentation.core.Green
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.components.TopBarIconOptions
import com.apollo9921.quizrise.presentation.core.QuizAppTheme

data class WrongAnswerModel(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String
)

@Composable
fun WrongAnswersRoute(
    navHostController: NavHostController,
    question: Array<String>,
    incorrectAnswers: Array<String>,
    correctAnswers: Array<String>
) {
    val wrongAnswers = question.mapIndexed { index, question ->
        WrongAnswerModel(
            question = question,
            userAnswer = incorrectAnswers[index],
            correctAnswer = correctAnswers[index]
        )
    }

    WrongAnswersScreen(
        onBackClick = { navHostController.navigateUp() },
        wrongAnswers = wrongAnswers
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WrongAnswersScreen(
    onBackClick: () -> Unit,
    wrongAnswers: List<WrongAnswerModel>
) {
    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = stringResource(R.string.revision),
                backIconOption = TopBarIconOptions.BACK,
                onClick = { onBackClick() }
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(wrongAnswers) { item ->
                    WrongAnswerCard(item)
                }
            }
        }
    }
}

@Composable
private fun WrongAnswerCard(item: WrongAnswerModel) {
    val colorError = MaterialTheme.colorScheme.onError
    val colorSuccess = Green

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.06f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = item.question,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AnswerFeedbackRow(
                    answerText = item.userAnswer,
                    isCorrect = false,
                    accentColor = colorError
                )

                AnswerFeedbackRow(
                    answerText = item.correctAnswer,
                    isCorrect = true,
                    accentColor = colorSuccess
                )
            }
        }
    }
}

@Composable
private fun AnswerFeedbackRow(
    answerText: String,
    isCorrect: Boolean,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = accentColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = answerText.ifEmpty { stringResource(R.string.no_answered) },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewWrongAnswersScreen() {
    QuizAppTheme {
        WrongAnswersScreen(
            onBackClick = {},
            wrongAnswers = listOf(
                WrongAnswerModel(
                    question = "What is the capital of France?",
                    userAnswer = "Paris",
                    correctAnswer = "London"
                ),
                WrongAnswerModel(
                    question = "What is the capital of Spain?",
                    userAnswer = "Madrid",
                    correctAnswer = "Barcelona"
                )
            )
        )
    }
}