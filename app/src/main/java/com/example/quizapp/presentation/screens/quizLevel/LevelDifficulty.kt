package com.example.quizapp.presentation.screens.quizLevel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.Black
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White

@Composable
fun LevelDifficulty(navHostController: NavHostController, category: String) {
    val context = LocalContext.current
    val levelsDifficulty = listOf(
        R.string.easy,
        R.string.medium,
        R.string.hard
    )
    val level = remember { mutableStateOf("") }
    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(levelsDifficulty[0]) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navHostController.navigateUp() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(id = R.string.chooseLevel),
                color = White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                levelsDifficulty.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption == item),
                                onClick = {
                                    onOptionSelected(item)
                                    level.value = context.resources.getString(item)
                                }
                            )
                            .padding(horizontal = 4.dp)
                    ) {
                        RadioButton(
                            selected = (selectedOption == item),
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = White,
                                unselectedColor = Black,
                                disabledSelectedColor = White,
                                disabledUnselectedColor = Black
                            )
                        )
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = stringResource(id = item),
                            color = White,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (level.value.isBlank()) {
                        level.value = context.resources.getString(levelsDifficulty[0])
                    }
                    navHostController.navigate(
                        Destination.StartQuiz.passArgument(category, level.value)
                    )
                },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(width = 2.dp, color = White),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(56.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(id = R.string.getStarted),
                    color = White
                )
            }
        }
    }
}