package com.example.quizapp.view.onBoard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.quizapp.R
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.main.isSplashScreenOpen
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.Purple40
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White
import kotlinx.coroutines.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoard(navHostController: NavHostController) {
    isSplashScreenOpen = false
    val pageCount = 5
    val state = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
    ) {
        HorizontalPager(
            pageCount = pageCount,
            state = state
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    onBoardingAnimation[it]
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(
                        if (mediaQueryWidth() <= small) {
                            300.dp
                        } else if (mediaQueryWidth() <= normal) {
                            400.dp
                        } else {
                            500.dp
                        }
                    )
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                }
                Text(
                    text = stringResource(id = onBoardingTitle[it]),
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
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
                )
                if (it == pageCount - 1) {
                    Button(
                        onClick = {
                            navHostController.popBackStack()
                            navHostController.navigate(Destination.CreateUser.route)
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
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                if (it > 0) {
                    Text(
                        text = stringResource(id = R.string.back),
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
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(40.dp)
                            .clickable {
                                coroutineScope.launch { state.animateScrollToPage(it - 1) }
                            }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                if (it < pageCount - 1) {
                    Text(
                        text = stringResource(id = R.string.next),
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
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(40.dp)
                            .clickable {
                                coroutineScope.launch { state.animateScrollToPage(it + 1) }
                            }
                    )
                }
            }
        }

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (state.currentPage == iteration) White else Purple40
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(
                            if (mediaQueryWidth() <= small) {
                                20.dp
                            } else if (mediaQueryWidth() <= normal) {
                                40.dp
                            } else {
                                60.dp
                            }
                        )
                )
            }
        }
    }
}