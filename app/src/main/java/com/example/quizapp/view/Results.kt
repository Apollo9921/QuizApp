package com.example.quizapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R
import com.example.quizapp.model.database.QuizDatabase
import com.example.quizapp.model.results.Results
import com.example.quizapp.model.user.User
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.theme.White

private lateinit var user: SnapshotStateList<User>
private lateinit var results: SnapshotStateList<Results>
private lateinit var data: Map<String, Int>

@Composable
fun Results() {
    user = remember { mutableStateListOf() }
    results = remember { mutableStateListOf() }
    QuizDatabase.getDatabase(LocalContext.current)
        .userDao().getUserProfile()
        .observe(LocalLifecycleOwner.current) {
            if (user.isNotEmpty()) {
                user.clear()
            }
            user.add(it)
        }
    QuizDatabase.getDatabase(LocalContext.current)
        .resultsDao().getResults()
        .observe(LocalLifecycleOwner.current) {
            if (results.isNotEmpty()) {
                results.clear()
            }
            for (i in it.indices) {
                results.add(it[i])
            }
        }

    if(user.isNotEmpty() && results.isNotEmpty()) {
        ShowResults()
    }
}

@Composable
private fun ShowResults() {
    for (i in results.indices) {
        val correct = results[i].correctAnswers
        val incorrect = results[i].incorrectAnswers
        if (i == 0) {
            data = if (correct == 0 && incorrect == 0) {
                mapOf(stringResource(id = categories[i]) to 0)
            } else if (incorrect == 0 && correct > 0) {
                mapOf(stringResource(id = categories[i]) to 100)
            } else if (correct == 0 && incorrect > 0) {
                mapOf(stringResource(id = categories[i]) to 0)
            } else {
                val percentage = (correct * 100) / (correct + incorrect)
                mapOf(stringResource(id = categories[i]) to percentage)
            }
        } else {
            data = if (correct == 0 && incorrect == 0) {
                data + mapOf(stringResource(id = categories[i]) to 0)
            } else if (incorrect == 0 && correct > 0) {
                data + mapOf(stringResource(id = categories[i]) to 100)
            } else if (correct == 0 && incorrect > 0) {
                data + mapOf(stringResource(id = categories[i]) to 0)
            } else {
                val percentage = (correct * 100) / (correct + incorrect)
                data + mapOf(stringResource(id = categories[i]) to percentage)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(
                        id = R.string.totalAndTotalPossiblePoints,
                        formatTotalCount(user[0].totalPoints.toFloat()).toInt(),
                        formatTotalCount(user[0].totalPointsPossible.toFloat()).toInt()
                    ),
                    color = White,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        25.sp
                    } else if (mediaQueryWidth() <= normal) {
                        30.sp
                    } else {
                        35.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            PieChart(
                data = data
            )
        }
    }
}