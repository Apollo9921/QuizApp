package com.example.quizapp.view.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.internet.checkInternetConnection
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.Purple40
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context
private var level = mutableStateOf("")
private var noWifi = ""

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LevelDifficulty(navHostController: NavHostController, category: String) {
    context = LocalContext.current
    noWifi = stringResource(id = R.string.noInternet)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(levelsDifficulty[0]) }
    BackHandler {
        navHostController.popBackStack(Destination.Main.route, inclusive = true)
        navHostController.navigate(Destination.Main.route)
    }
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
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .size(
                        if (mediaQueryWidth() <= small) {
                            75.dp
                        } else if (mediaQueryWidth() <= normal) {
                            100.dp
                        } else {
                            125.dp
                        }
                    )
                    .clickable {
                        navHostController.popBackStack(Destination.Main.route, inclusive = true)
                        navHostController.navigate(Destination.Main.route)
                    }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.chooseLevel),
                color = White,
                fontSize =
                if (mediaQueryWidth() <= small) {
                    30.sp
                } else if (mediaQueryWidth() <= normal) {
                    40.sp
                } else {
                    50.sp
                },
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                levelsDifficulty.forEach { item ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (selectedOption == item),
                                onClick = {
                                    onOptionSelected(item)
                                    level.value = context.resources.getString(item)
                                }
                            )
                    ) {
                        RadioButton(
                            selected = (selectedOption == item),
                            onClick = {
                                onOptionSelected(item)
                                level.value = context.resources.getString(item)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = White,
                                unselectedColor = Black,
                                disabledSelectedColor = White,
                                disabledUnselectedColor = Black
                            ),
                            modifier = Modifier
                                .size(
                                    if (mediaQueryWidth() <= small) {
                                        45.dp
                                    } else if (mediaQueryWidth() <= normal) {
                                        50.dp
                                    } else {
                                        55.dp
                                    }
                                )
                        )
                        Text(
                            text = stringResource(id = item),
                            color = White,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                15.sp
                            } else if (mediaQueryWidth() <= normal) {
                                20.sp
                            } else {
                                25.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 15.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                onClick = {
                    internet.value = checkInternetConnection(context)
                    if (internet.value) {
                        navHostController.navigate(
                            Destination.StartQuiz.passArgument(category, level.value)
                        )
                    } else {
                        Toast.makeText(context, noWifi, Toast.LENGTH_LONG).show()
                    }
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
                    text = stringResource(id = R.string.getStarted),
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